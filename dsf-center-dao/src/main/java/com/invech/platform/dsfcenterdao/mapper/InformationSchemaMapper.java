package com.invech.platform.dsfcenterdao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: InformationSchemaMapper
 * @Author: R.M.I
 * @CreateTime: 2019年03月08日 16:29:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Mapper
public interface InformationSchemaMapper  {

    public String selectBetlogTable(@Param("schemaName") String schemaName,@Param("tablePrefix") String tablePrefix);
}
