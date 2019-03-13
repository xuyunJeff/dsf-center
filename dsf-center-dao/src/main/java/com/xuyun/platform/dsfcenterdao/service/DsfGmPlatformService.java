package com.xuyun.platform.dsfcenterdao.service;


import com.xuyun.platform.dsfcenterdao.mapper.DsfGmPlatformMapper;
import com.xuyun.platform.dsfcenterdao.master.DsfGmApiMapper;
import com.xuyun.platform.dsfcenterdao.master.DsfGmApiPrefixMapper;
import com.xuyun.platform.dsfcenterdao.master.DsfSiteMapper;
import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.entity.*;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.response.R;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApiPrefix;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmPlatform;
import com.xuyun.platform.dsfcenterdata.entity.DsfSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DsfGmPlatformService extends BaseService<DsfGmPlatformMapper, DsfGmPlatform> {

    @Autowired
    DsfGmPlatformMapper dsfGmPlatformMapper;

    @Autowired
    DsfGmPlatformService dsfGmPlatformService;

    @Autowired
    DsfGmApiMapper dsfGmApiMapper;

    @Autowired
    DsfGmApiPrefixMapper dsfGmApiPrefixMapper;

    @Autowired
    DsfSiteMapper dsfSiteMapper;

    public List<DsfGmPlatform> platformList(GamePlatform gamePlatform, String siteCode) {
        // 走redis缓存
        List<DsfGmPlatform> redisPlatList = dsfGmPlatformService.platList(gamePlatform, siteCode);
        return redisPlatList;
    }


    @CachePut(cacheNames = ApiConstants.REDIS_PLAT_LIST_KEY, key = "#siteCode")
    public List<DsfGmPlatform> platList(GamePlatform gamePlatform, String siteCode) {
        DsfGmPlatform gmPlatform = new DsfGmPlatform();
        if (gamePlatform != null) {
            gmPlatform.setPlatformCode(gamePlatform.platformCode);
        }
        return dsfGmPlatformMapper.select(gmPlatform);

    }


    public R savePlatform(DsfGmPlatform dsfGmPlatform, Long memberId) {
        DsfGmPlatform gm = new DsfGmPlatform();
        gm.setPlatformName(dsfGmPlatform.getPlatformName());
        DsfGmPlatform gmPlatform = dsfGmPlatformMapper.selectOne(gm);
        if (gmPlatform == null) {
            dsfGmPlatform.setModifyUser(memberId.toString());
            dsfGmPlatform.setCreateTime(LocalDateTime.now().toString());
            dsfGmPlatform.setModifyTime(LocalDateTime.now().toString());
            dsfGmPlatform.setCreateUser(memberId.toString());
            dsfGmPlatformMapper.insert(dsfGmPlatform);
            return R.ok();
        } else {
            return R.error(ErrorCode.DSF_PLAT_FORM_REPEAT.getCode(), ErrorCode.DSF_PLAT_FORM_REPEAT.getMsg());
        }

    }


    public R savePlatformPrefix(String platformCode, String prefix, String siteCode, Long memerId) {
        DsfGmApi api = new DsfGmApi();
        api.setPlatformCode(platformCode);
        api.setAvailable(ApiConstants.ENABLE);
        DsfGmApi dsfGmApi = dsfGmApiMapper.selectOne(api);
        DsfSite dsfSite = new DsfSite();
        dsfSite.setSiteCode(siteCode);
        //dsfSite.setAvailable(ApiConstants.ENABLE);
        DsfSite site = dsfSiteMapper.selectOne(dsfSite);
        DsfGmApiPrefix apiPrefix = new DsfGmApiPrefix();
        apiPrefix.setPrefix(prefix);
        apiPrefix.setSiteId(site.getId());
        DsfGmApiPrefix gmApiPrefix = dsfGmApiPrefixMapper.selectOne(apiPrefix);
        if (dsfGmApi == null) {
            return R.error(ErrorCode.DSF_NOT_API_PLAT.getCode(), ErrorCode.DSF_NOT_API_PLAT.getMsg());
        } else if (site == null) {
            return R.error(ErrorCode.DSF_NOT_SITE_PLAT.getCode(), ErrorCode.DSF_NOT_SITE_PLAT.getMsg());
        } else {
            if (gmApiPrefix == null) {
                DsfGmApiPrefix gm = new DsfGmApiPrefix();
                gm.setAvailable(ApiConstants.ENABLE);
                gm.setApiId(dsfGmApi.getId());
                gm.setPrefix(prefix);
                gm.setSiteId(site.getId());
                gm.setModifyTime(LocalDateTime.now().toString());
                gm.setCreateUser(memerId.toString());
                gm.setModifyUser(memerId.toString());
                gm.setCreateTime(LocalDateTime.now().toString());
                dsfGmApiPrefixMapper.insert(gm);
                return R.ok();
            } else {
                return R.error(ErrorCode.DSF_PREFIX_EXIT.getCode(), ErrorCode.DSF_PREFIX_EXIT.getMsg());
            }
        }

    }
}


