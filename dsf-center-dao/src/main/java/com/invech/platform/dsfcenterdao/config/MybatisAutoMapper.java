package com.invech.platform.dsfcenterdao.config;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.provider.SpecialProvider;

import java.util.List;

public interface MybatisAutoMapper<T> extends MySqlMapper<T>,IdsMapper<T>,BaseMapper<T>,ReplaceIntoListMapper<T> {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = InsertIgnoreListProvider.class, method = "dynamicSQL")
    int insertIgnoreList(List<T> recordList);

}