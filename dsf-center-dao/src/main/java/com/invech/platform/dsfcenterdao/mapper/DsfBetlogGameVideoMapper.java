package com.invech.platform.dsfcenterdao.mapper;

import com.invech.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGameVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: DsfBetlogGameVideoMapper
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 20:28:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Mapper
public interface DsfBetlogGameVideoMapper extends MybatisAutoMapper<DsfBetlogGameVideo> {


    public List<DsfBetlogGameVideo> videoGameBetlogsBatch(@Param("size") Integer size,@Param("lastUpdateId") Long lastUpdateId,@Param("month") String yyyyMM);

    public int createvideoGameBetlog(@Param("month") String yyyyMM);

    public int createvideoGameBetlogItem(@Param("month") String yyyyMM);
}
