package com.xuyun.platform.dsfcenterdao.service;

import com.xuyun.platform.dsfcenterdao.master.dao.SysMapper;
import com.xuyun.platform.dsfcenterdao.master.DsfSchemaMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DsfSchemaService extends BaseService<DsfSchemaMapper, DsfSchema> {

    @Autowired
    DsfSchemaMapper TSchemaMapper;

    @Autowired
    SysMapper sysMapper;

    public void insertTschema(DsfSchema TSchema){
        super.save(TSchema);
    }

    public void updateTschema(DsfSchema TSchema){
        super.update(TSchema);
    }

    public DsfSchema selectOne(DsfSchema TSchema){
        return TSchemaMapper.selectOne(TSchema);
    }

    @Override
    public int selectCount(DsfSchema TSchema){
        return super.selectCount(TSchema);
    }

    public int updateTschemaSiteCode(DsfSchema TSchema) {
        return sysMapper.updateTschemaSiteCode(TSchema);
    }
}
