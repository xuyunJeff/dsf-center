package com.xuyun.platform.dsfcenterdao.mapper;

import com.xuyun.platform.dsfcenterdao.config.MybatisAutoMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: DsfMemberUserMapper
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 17:48:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Mapper
public interface DsfMemberUserMapper extends MybatisAutoMapper<DsfMemberUser> {
    public List<DsfMemberUser> selectByDsfPlayerIds(List<String> playerIds);
}
