package com.invech.platform.dsfcenterdao.master;


import com.invech.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.invech.platform.dsfcenterdata.entity.DsfSite;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface DsfSiteMapper extends MybatisAutoMapper<DsfSite> { }