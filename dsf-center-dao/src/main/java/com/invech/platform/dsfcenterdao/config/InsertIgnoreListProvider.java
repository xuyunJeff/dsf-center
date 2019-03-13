package com.invech.platform.dsfcenterdao.config;

import com.invech.platform.dsfcenterdata.utils.DateUtil;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.SpecialProvider;
import tk.mybatis.mapper.util.StringUtil;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @ClassName: InsertIgnoreListProvider
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 20:55:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class InsertIgnoreListProvider extends SpecialProvider {

    public InsertIgnoreListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }
    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertIgnoreList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(InsertIgnoreListProvider.insertIgnoreIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * insert into tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String insertIgnoreIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT ignore INTO ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    /**
     * 按月份分表逻辑
     * @param entityClass
     * @return
     */
    @Override
    protected String tableName(Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (StringUtil.isEmpty(prefix)) {
            //使用全局配置
            prefix = mapperHelper.getConfig().getPrefix();
        }
        if (StringUtil.isNotEmpty(prefix)) {
            return prefix + "." + entityTable.getName();
        }
        String tableStuff = "_"+ DateUtil.currentMonth();
        return entityTable.getName()+tableStuff;
    }
}
