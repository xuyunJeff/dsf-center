package com.invech.platform.dsfcenterdao.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.invech.platform.dsfcenterdao.master.DsfGmApiPrefixMapper;
import com.invech.platform.dsfcenterdao.master.DsfSchemaMapper;
import com.invech.platform.dsfcenterdao.master.DsfSiteSchemaMapper;
import com.invech.platform.dsfcenterdao.master.dao.SysMapper;
import com.invech.platform.dsfcenterdao.master.DsfSiteMapper;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.*;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invech.platform.dsfcenterdata.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DsfSiteService extends BaseService<DsfSiteMapper, DsfSite> {

    @Autowired
    DsfSchemaService tschemaService;

    @Autowired
    SysMapper sysMapper;

    @Autowired
    DsfSiteSchemaMapper dsfSiteSchemaMapper;

    @Autowired
    DsfSiteMapper dsfSiteMapper;

    @Autowired
    DsfSchemaMapper dsfSchemaMapper;

    @Autowired
    DsfGmApiPrefixMapper dsfGmApiPrefixMapper;


    /**
     *
     * 游戏平台+site 获取对应的线路
     * @param siteCode
     * @return
     */
    @Cacheable(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#siteCode")
    public List<DsfGmApi> queryApisBySiteCodePlatform(String siteCode) {
        return sysMapper.findApis(siteCode);
    }


//    /**
//     * 游戏平台+site 获取对应的线路
//     *
//     * @param siteCode
//     * @param gamePlatformCode
//     * @return
//     */
//    @Cacheable(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#siteCode+':'+#gamePlatformCode")
//    public List<DsfGmApi> queryApisBySiteCodePlatform(String siteCode, String gamePlatformCode) {
//        return sysMapper.findApis(siteCode, gamePlatformCode);
//    }

    /**
     * 获取所有的线路,及其配置,线路的前缀,siteCode和线路的对应关系
     * 拉单使用,不走缓存
     * @return
     */
    public List<DsfGmApi> queryApis() {
        return sysMapper.findApis(null);
    }

    @Cacheable(cacheNames = ApiConstants.REDIS_GAME_SITECODE_CACHE_KEY, key = "#siteCode")
    public DsfSite getSchemaName(String siteCode) {
        DsfSite site = new DsfSite();
        site.setSiteCode(siteCode);
        return super.queryObjectCond(site);
    }

    /**
     * 添加SiteCode
     *
     * @param dsfSite
     * @param memberId
     * @param schemaId
     * @return
     */
    @CacheEvict(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#dsfSite.siteCode")
    @Transactional
    public R addSiteCode(DsfSite dsfSite, Long memberId, Integer schemaId) {
        if(StringUtils.isEmpty(dsfSite.getSiteCode())){
            return  R.error(ErrorCode.DSF_SITE_CODE_CANNOT_NULL.getCode(),ErrorCode.DSF_SITE_CODE_CANNOT_NULL.getMsg());
        }
        if (dsfSite.getSiteCode().length() > 4) {
            return R.error(ErrorCode.DSF_PREFIX_LONG.getCode(), ErrorCode.DSF_PREFIX_LONG.getMsg());
        }
        if (dsfSite.getCurrency().length() > 4) {
            return R.error(ErrorCode.DSF_PARAM_TOO_LONG.getCode(), ErrorCode.DSF_PARAM_TOO_LONG.getMsg());
        }
        DsfSite sfSite = new DsfSite();
        sfSite.setSiteCode(dsfSite.getSiteCode());
        List<DsfSite> siteList = dsfSiteMapper.select(sfSite);
        if(siteList.size() > 0){
            return R.error(ErrorCode.DSF_EXSIT_SITE_CODE.getCode(),ErrorCode.DSF_EXSIT_SITE_CODE.getMsg());
        }
        DsfSchema schema = sysMapper.selectDsfSchemaOne();
        if (schema == null) {
            return R.error(ErrorCode.DSF_NOT_PRE_SITE.getCode(), ErrorCode.DSF_NOT_PRE_SITE.getMsg());
        }
        schema.setIsUsed(ApiConstants.ENABLE);
        int i = tschemaService.updateTschemaSiteCode(schema);
        if (i == 0) {
            return R.error(ErrorCode.DSF_SITE_PREFIX_USED.getCode(), ErrorCode.DSF_SITE_PREFIX_USED.getMsg());
        }
        /*插入sitecode 和 schemaName 的对应关系*/
        DsfSite tSite = new DsfSite();
        tSite.setSiteCode(dsfSite.getSiteCode());
        tSite.setSchemaName(schema.getSchemaName());
        tSite.setIsApi(Byte.valueOf("0"));
        tSite.setAvailable(dsfSite.getAvailable());
        String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_18_DATE_TIME);
        tSite.setCreateTime(currentDate);
        tSite.setMemo(dsfSite.getSiteName());
        tSite.setCurrency(dsfSite.getCurrency());
        tSite.setSiteName(dsfSite.getSiteName());
        tSite.setCreateUser(memberId.toString());
        tSite.setModifyTime(currentDate);
        tSite.setStartDate(dsfSite.getStartDate());
        tSite.setEndDate(dsfSite.getEndDate());
        tSite.setCompanyId(dsfSite.getCompanyId());
        super.save(tSite);
        //插入中间表dsf_site_schema
        DsfSiteSchema dsfSiteSchema = new DsfSiteSchema();
        dsfSiteSchema.setSiteId(tSite.getId());
        dsfSiteSchema.setSchemaId(schemaId);
        dsfSiteSchema.setCreateUser(memberId.toString());
        dsfSiteSchema.setModifyUser(memberId.toString());
        dsfSiteSchema.setCreateTime(currentDate);
        dsfSiteSchema.setModifyTime(currentDate);
        dsfSiteSchema.setAvaliable(tSite.getAvailable());
        dsfSiteSchemaMapper.insert(dsfSiteSchema);
        /*插入apiPrefix 和siteCode 的对应关系*/
        DsfGmApiPrefix prefix = new DsfGmApiPrefix();
        prefix.setSiteId(tSite.getId());
        List<DsfGmApiPrefix> models = dsfGmApiPrefixMapper.select(prefix);
        models.forEach(model -> {
            sysMapper.insertApiPrefix(
                    new DsfGmApiPrefix(model.getApiId(), model.getPrefix().replace("model", dsfSite.getSiteCode()),
                            tSite.getId(), new Byte("1"), memberId.toString(), memberId.toString()));
        });
        return R.ok();
    }

    @CacheEvict(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#siteCode")
    public R deleteSite(Integer siteId,String siteCode) {
        DsfSite dsfSite = dsfSiteMapper.selectByPrimaryKey(siteId);
        if (dsfSite == null) {
            return R.error(ErrorCode.DSF_NOT_FOUND_SITE.getCode(), ErrorCode.DSF_NOT_FOUND_SITE.getMsg());
        }
        //更新site表数据状态
        dsfSite.setAvailable(ApiConstants.DISABLE);
        dsfSite.setId(siteId);
        dsfSiteMapper.updateByPrimaryKey(dsfSite);
        //更新schema表数据状态
        //更新dsf_site_schema表状态
        DsfSiteSchema dsfSiteSchema = new DsfSiteSchema();
        dsfSiteSchema.setSiteId(siteId);
        DsfSiteSchema siteSchema = dsfSiteSchemaMapper.selectOne(dsfSiteSchema);
        if (siteSchema != null) {
            siteSchema.setAvaliable(ApiConstants.DISABLE);
            dsfSiteSchemaMapper.updateByPrimaryKeySelective(siteSchema);
            DsfSchema dsSchema = dsfSchemaMapper.selectByPrimaryKey(siteSchema.getSchemaId());
            if (dsSchema != null) {
                dsSchema.setIsUsed(ApiConstants.DISABLE);
                dsfSchemaMapper.updateByPrimaryKey(dsSchema);
            }
        }
        return R.ok();
    }

    public List<DsfSite> getSitesList() {
        return dsfSiteMapper.selectAll();
    }

    public PageInfo getSitesList(Integer pageNo, Integer pageSize, String siteName,String siteCode) {
        PageHelper.startPage(pageNo, pageSize);
        DsfSite dsfSite = new DsfSite();
        if (!StringUtils.isEmpty(siteName)) {
            dsfSite.setSiteName(siteName);
        }
        if (!StringUtils.isEmpty(siteCode)) {
            dsfSite.setSiteCode(siteCode);
        }
        dsfSite.setAvailable(ApiConstants.ENABLE);
        List<DsfSite> dsfSitesList = dsfSiteMapper.select(dsfSite);
        PageInfo<DsfSite> pageInfo = new PageInfo<>(dsfSitesList);
        return pageInfo;
    }

    @CacheEvict(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#dsfSite.siteCode")
    @Transactional
    public R editSite(DsfSite dsfSite, Long memberId) {
        if(StringUtils.isEmpty(dsfSite.getSiteCode())){
            return  R.error(ErrorCode.DSF_SITE_CODE_CANNOT_NULL.getCode(),ErrorCode.DSF_SITE_CODE_CANNOT_NULL.getMsg());
        }
        if (dsfSite.getCurrency().length() > 4) {
            return R.error(ErrorCode.DSF_PARAM_TOO_LONG.getCode(), ErrorCode.DSF_PARAM_TOO_LONG.getMsg());
        }
        if (dsfSite.getSiteCode().length() > 4) {
            return R.error(ErrorCode.DSF_PREFIX_LONG.getCode(), ErrorCode.DSF_PREFIX_LONG.getMsg());
        }
        DsfSite sf = dsfSiteMapper.selectByPrimaryKey(dsfSite.getId());
        if (sf == null) {
            return R.error(ErrorCode.DSF_NOT_FOUND_SITE.getCode(), ErrorCode.DSF_NOT_FOUND_SITE.getMsg());
        }
        sf.setModifyTime(LocalDateTime.now().toString());
        sf.setModifyUser(memberId.toString());
        sf.setSiteCode(dsfSite.getSiteCode());
        sf.setSchemaName(dsfSite.getSchemaName());
        sf.setIsApi(dsfSite.getIsApi());
        sf.setAvailable(dsfSite.getAvailable());
        sf.setSiteName(dsfSite.getSiteName());
        sf.setCompanyId(dsfSite.getCompanyId());
        sf.setCurrency(dsfSite.getCurrency());
        sf.setMemo(dsfSite.getMemo());
        sf.setStartDate(dsfSite.getStartDate());
        sf.setEndDate(dsfSite.getEndDate());
        sf.setUseTime(dsfSite.getUseTime());
        sf.setCompanyUser(dsfSite.getCompanyUser());
        dsfSiteMapper.updateByPrimaryKey(sf);
        return R.ok();
    }

    @CacheEvict(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#siteCode")
    public R enabledSite(Integer siteId, Boolean available, Long memberId,String siteCode) {
        DsfSite df = new DsfSite();
        df.setId(siteId);
        DsfSite site = dsfSiteMapper.selectOne(df);
        if(site == null){
          return  R.error(ErrorCode.DSF_NOT_FOUND_SITE.getCode(),ErrorCode.DSF_NOT_FOUND_SITE.getMsg());
        }else{
            if(available == true){
                site.setAvailable(ApiConstants.ENABLE);
            } else {
                site.setAvailable(ApiConstants.DISABLE);
            }
            site.setModifyTime(LocalDateTime.now().toString());
            site.setModifyUser(memberId.toString());
            dsfSiteMapper.updateByPrimaryKey(site);
            return R.ok();
        }
    }

    @CacheEvict(cacheNames = ApiConstants.GAME_PLATFORM_API, key = "#dsfSite.siteCode")
    public R addSite(DsfSite dsfSite, Long memberId) {
        dsfSite.setIsApi(ApiConstants.BW);
        dsfSite.setCreateUser(memberId.toString());
        dsfSite.setModifyUser(memberId.toString());
        dsfSite.setModifyTime(LocalDateTime.now().toString());
        dsfSite.setCreateTime(LocalDateTime.now().toString());
        dsfSiteMapper.insert(dsfSite);
        return R.ok();
    }

}
