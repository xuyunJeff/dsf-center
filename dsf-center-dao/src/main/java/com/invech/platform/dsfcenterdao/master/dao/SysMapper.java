package com.invech.platform.dsfcenterdao.master.dao;


import com.invech.platform.dsfcenterdata.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMapper {

  List<DsfGmApi> findApis(@Param("siteCode") String siteCode);

  List<DsfSite> findSite();

  DsfGmApi findGmApiOne(@Param("depotId") Integer depotId, @Param("siteCode") String siteCode);

  Integer updateSchema(@Param("id") Integer id, @Param("siteCode") String siteCode);


  DsfSchema selectDsfSchemaOne();

/*  List<DsfSysConfig> listSysConfig(@Param("groups") String groups);*/

  String getSiteCode(@Param("siteCode") String siteCode);

  int updateTschemaSiteCode(DsfSchema tSchema);

  int insertApiPrefix(DsfGmApiPrefix tGmApiPrefix);

  List<DsfGmApiPrefix> selectApiPrefixByModel();

  List<DsfGmApiPrefix> selectPrefixByPrefixOrSiteCode(@Param("prefix") String prefix,@Param("siteCode") String siteCode);

  List<DsfGmGame> findGameListAg(@Param("paramCodes")  List<String> paramCodes, @Param("platformCode")  String platformCode);

}
