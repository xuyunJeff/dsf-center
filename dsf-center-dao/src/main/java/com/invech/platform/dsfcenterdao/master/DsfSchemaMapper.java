package com.invech.platform.dsfcenterdao.master;

import com.invech.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.invech.platform.dsfcenterdata.entity.DsfSchema;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsfSchemaMapper extends MybatisAutoMapper<DsfSchema> { }
