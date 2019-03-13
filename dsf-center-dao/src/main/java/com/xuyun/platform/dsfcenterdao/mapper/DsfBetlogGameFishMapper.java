package com.xuyun.platform.dsfcenterdao.mapper;

import com.xuyun.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogGameFish;
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
public interface DsfBetlogGameFishMapper extends MybatisAutoMapper<DsfBetlogGameFish> {


    public List<DsfBetlogGameFish> fishGameBetlogsBatch(@Param("size") Integer size, @Param("lastUpdateId") Long lastUpdateId, @Param("month") String yyyyMM);

    public int createFishGameBetlog(@Param("month") String yyyyMM);
}
