package com.invech.platform.dsfcenterdao.mapper;

import com.invech.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGamePoker;
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
public interface DsfBetlogGamePokerMapper extends MybatisAutoMapper<DsfBetlogGamePoker> {


    public List<DsfBetlogGamePoker> pokerGameBetlogsBatch(@Param("size") Integer size, @Param("lastUpdateId") Long lastUpdateId, @Param("month") String yyyyMM);

    public int createPokerGameBetlog(@Param("month") String yyyyMM);
}
