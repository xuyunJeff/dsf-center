package com.xuyun.platform.dsfcenterdao.mapper;

import com.xuyun.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DsfTransferLogMapper
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 14:34:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Mapper
public interface DsfTransferLogMapper extends MybatisAutoMapper<DsfTransferLog> {
    public int transferSuccessOrFailed(DsfTransferLog transferLog);

    public int transferUnKnow(DsfTransferLog transferLog);
}
