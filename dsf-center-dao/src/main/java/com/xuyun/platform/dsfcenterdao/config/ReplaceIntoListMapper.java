package com.xuyun.platform.dsfcenterdao.config;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * @ClassName: ReplaceIntoListMapper
 * @Author: R.M.I
 * @CreateTime: 2019年03月04日 18:05:00
 * @Description: TODO
 * @Version 1.0.0
 */
public interface ReplaceIntoListMapper<T>  {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = ReplaceIntoListProvider.class, method = "dynamicSQL")
    int replaceIntoList(List<T> recordList);
}
