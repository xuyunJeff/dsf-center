package com.xuyun.platform.dsfcenterdao.master;


import com.xuyun.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfSiteSchema;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface DsfSiteSchemaMapper extends MybatisAutoMapper<DsfSiteSchema> { }