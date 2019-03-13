package com.invech.platform.dsfcenterdao.config;

import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis 全局sql过滤
 * mybatis 的insert 和 delete 也是调用了 update方法
 * Created by Ryan on 2019/01/20.
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class})})
public class MybatisInterceptor implements Interceptor {

    private final static String WHITE_MAPPER_METHOD = "com.invech.platform.dsfcenterdao.master" ;

    private final static String MYBATIS_EXECUTE_SQL = "delegate.boundSql.sql";

    private final static String MYCAT_PREFIX = "/*!mycat:schema = %s */ %s ";
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //获取statementHandler包装类
        MetaObject metaobjecthandler = SystemMetaObject.forObject(statementHandler);
        //获取查询接口映射的相关信息
        MappedStatement mappedStatement = (MappedStatement) metaobjecthandler.getValue("delegate.mappedStatement");
        //获取进行数据库操作时管理参数的handler ,ParameterHandler parameterHandler = (ParameterHandler) MetaObjectHandler.getValue("delegate.parameterHandler");
        String mapId = mappedStatement.getId();
        //获取sql
        String sql = (String) metaobjecthandler.getValue(MYBATIS_EXECUTE_SQL);
        String schema = ApiConstants.MASTER_SCHEMA_SUFFIX;

        if(!mapId.startsWith(WHITE_MAPPER_METHOD)){
//            String siteSchema = SiteUtil.getSchemaName();
            SiteUtil siteUtil = SpringContextUtil.getBean("SiteUtil");
            String schemaName = siteUtil.getSchemaName();
            if(!StringUtils.isEmpty(schemaName)){
                //同步线程时的代码
                schema =schemaName;
            }else if(ThreadLocalCache.schemaThreadLocal.get() !=null && ThreadLocalCache.schemaThreadLocal.get().getSchemaName() != null){
                //异步线程时的代码
                schema = ThreadLocalCache.schemaThreadLocal.get().getSchemaName();
            }
        }
        //获取站点前缀
        metaobjecthandler.setValue(MYBATIS_EXECUTE_SQL, String.format(MYCAT_PREFIX,schema.toLowerCase(),sql));
         log.info("sql = {}", "/*!mycat:schema = "+schema.toLowerCase()+" */"+sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        log.info(properties.toString());
    }

}
