package com.invech.platform.dsfcenterservice.service;

import com.invech.platform.dsfcenterdao.mapper.DsfGmGameMapper;
import com.invech.platform.dsfcenterdao.mapper.DsfMemberUserMapper;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.response.RedisPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rmi
 */
@Service
public class AdminManageGamesService {

    @Autowired
    private DsfGmGameMapper tGmGameMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    AdminManageGamesService adminManageGamesService;

    @Autowired
    DsfMemberUserMapper dsfMemberUserMapper;

    public RedisPageHelper<DsfGmGame> gameListPage(GamePlatform gamePlatform, String siteCode, Integer pageNo, Integer pageSize) {
        // 走redis缓存，自己实现分页
        List<DsfGmGame> dsfGmGames = adminManageGamesService.gameList(gamePlatform, siteCode);
        return new RedisPageHelper<DsfGmGame>().getPage(dsfGmGames, pageNo, pageSize);
    }

    @Cacheable(cacheNames = ApiConstants.REDIS_GAME_LIST_KEY, key = "#siteCode+':'+#gamePlatform")
    public List<DsfGmGame> gameList(GamePlatform gamePlatform, String siteCode) {
        DsfGmGame t = new DsfGmGame();
        if (gamePlatform != null) {
            t.setPlatformCode(gamePlatform.platformCode);
        }
        return tGmGameMapper.select(t);
    }


    public R freezeUser(String memberUser, Boolean avaliable) {
        DsfMemberUser dsfMemberUser = new DsfMemberUser();
        dsfMemberUser.setMemberUser(memberUser);
        List<DsfMemberUser> dsfMemberList = dsfMemberUserMapper.select(dsfMemberUser);
        if (dsfMemberList.size() <= 0) {
            return R.error(ErrorCode.DSF_NOT_FOUND_MEMBER.getCode(), ErrorCode.DSF_NOT_FOUND_MEMBER.getMsg());
        }
        dsfMemberList.forEach(dsfMember -> {
            if (avaliable == true) {
                dsfMember.setAvailable(ApiConstants.EFFCTIVE);
            } else {
                dsfMember.setAvailable(ApiConstants.NOT_EFFCTIVE);
            }
            updateMemberUser(dsfMember);
        });

        return R.ok();
    }

    @CacheEvict(value = ApiConstants.REDIS_USER_CACHE_KEY, key = "#dsfMemberUser.memberUser")
    public void updateMemberUser(DsfMemberUser dsfMemberUser) {
        dsfMemberUser.setUpdateTime(LocalDateTime.now().toString());
        dsfMemberUserMapper.updateByPrimaryKey(dsfMemberUser);
    }

    public R freezeUserByPlatform(String platformCode, String memberUser) {
        DsfMemberUser dsfMemberUser = new DsfMemberUser();
        dsfMemberUser.setPlatformCode(platformCode);
        dsfMemberUser.setMemberUser(memberUser);
        DsfMemberUser dsfUser = dsfMemberUserMapper.selectOne(dsfMemberUser);
        if (dsfUser == null) {
            return R.error(ErrorCode.DSF_NOT_FOUND_MEMBER.getCode(), ErrorCode.DSF_NOT_FOUND_MEMBER.getMsg());
        }
        dsfUser.setAvailable(ApiConstants.NOT_EFFCTIVE);
        updateMemberUser(dsfUser);
        return R.ok();
    }

}
