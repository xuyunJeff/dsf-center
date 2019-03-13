package com.xuyun.platform.dsfcenterdao.service;

import com.xuyun.platform.dsfcenterdao.mapper.DsfGmGameMapper;
import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmGame;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class DsfGmGameService extends BaseService<DsfGmGameMapper, DsfGmGame> {
    @Autowired
    DsfGmGameMapper tGmGameMapper;
    @Autowired
    RedisTemplate redisTemplate;

    public R updateGameImgUrl(String gameId, String pcLogo, String phoneLogo, Long memberId) {
        DsfGmGame tGmGame = tGmGameMapper.selectByPrimaryKey(gameId);
        if (tGmGame == null) {
            return R.error(ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getCode(), ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getMsg());
        } else {
            tGmGame.setPcLogo(pcLogo);
            tGmGame.setPhoneLogo(phoneLogo);
            tGmGame.setModifyTime(LocalDateTime.now().toString());
            tGmGame.setModifyUser(memberId.toString());
            tGmGameMapper.updateByPrimaryKey(tGmGame);
            return R.ok();
        }

    }

    public R enableOrDisableGame(Integer gameId, Boolean available, Long memberId) {
        DsfGmGame dsfGmGame = new DsfGmGame();
        dsfGmGame.setId(gameId);
        DsfGmGame dsfGm = tGmGameMapper.selectByPrimaryKey(gameId);
        if (dsfGm == null) {
            return R.error(ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getCode(), ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getMsg());
        } else {
            dsfGm.setModifyTime(LocalDateTime.now().toString());
            dsfGm.setModifyUser(memberId.toString());
            if (available == true) {
                dsfGm.setAvailable(ApiConstants.ENABLE);
            } else {
                dsfGm.setAvailable(ApiConstants.DISABLE);
            }
            tGmGameMapper.updateByPrimaryKey(dsfGm);
            return R.ok();
        }
    }

    public R updateGame(Integer gameId, Integer sortId, Boolean enableHot, Boolean enableNew, Boolean available, Long memberId) {
        DsfGmGame dsfGmGame = tGmGameMapper.selectByPrimaryKey(gameId);
        if (dsfGmGame == null) {
            return R.error(ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getCode(), ErrorCode.DSF_NOT_SEARCH_GAME_INFO.getMsg());
        }
        if(sortId > 0){
            dsfGmGame.setSortId(sortId);
        }
        if (enableHot != null) {
            if (enableHot == true) {
                dsfGmGame.setEnableHot(ApiConstants.ENABLE);
            } else {
                dsfGmGame.setEnableHot(ApiConstants.DISABLE);
            }
        }
        if (enableNew != null) {
            if (enableNew == true) {
                dsfGmGame.setEbableNew(ApiConstants.ENABLE);
            } else {
                dsfGmGame.setEbableNew(ApiConstants.DISABLE);
            }
        }
        if (available != null) {
            if (available == true) {
                dsfGmGame.setAvailable(ApiConstants.ENABLE);
            } else {
                dsfGmGame.setAvailable(ApiConstants.DISABLE);
            }
        }
        dsfGmGame.setModifyTime(LocalDateTime.now().toString());
        dsfGmGame.setModifyUser(memberId.toString());
        tGmGameMapper.updateByPrimaryKey(dsfGmGame);
        return R.ok();
    }


}
