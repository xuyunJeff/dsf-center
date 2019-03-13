package com.invech.platform.dsfcenterdao.master;


import com.invech.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.invech.platform.dsfcenterdata.entity.DsfSiteSchema;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface DsfSiteSchemaMapper extends MybatisAutoMapper<DsfSiteSchema> { }