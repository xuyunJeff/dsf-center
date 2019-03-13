package com.xuyun.platform.dsfcenterdao.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuyun.platform.dsfcenterdao.master.DsfGmApiPrefixMapper;
import com.xuyun.platform.dsfcenterdao.master.DsfSiteMapper;
import com.xuyun.platform.dsfcenterdao.master.dao.SysMapper;
import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApiPrefix;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.R;
import com.xuyun.platform.dsfcenterdata.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DsfGmApiPrefixService extends BaseService<DsfGmApiPrefixMapper, DsfGmApiPrefix> {

    @Autowired
    DsfGmApiPrefixMapper dsfGmApiPrefixMapper;

    @Autowired
    DsfSiteMapper dsfSiteMapper;

    @Autowired
    DsfGmApiPrefixService dsfGmApiPrefixService;

    @Autowired
    SysMapper sysMapper;


    public R enableOrDisEnablePrefix(Integer prefixId, Boolean avaliable, Long memerId) {

        DsfGmApiPrefix dsfGmApiPrefix = dsfGmApiPrefixMapper.selectByPrimaryKey(prefixId);
        if (dsfGmApiPrefix == null) {
            return  R.error(ErrorCode.DSF_NOT_SEARCH__LINE.getCode(),ErrorCode.DSF_NOT_SEARCH__LINE.getMsg());
        } else {
            if (avaliable == true) {
                dsfGmApiPrefix.setAvailable(ApiConstants.ENABLE);

            } else {
                dsfGmApiPrefix.setAvailable(ApiConstants.DISABLE);
            }
        }
        dsfGmApiPrefix.setModifyTime(LocalDateTime.now().toString());
        dsfGmApiPrefix.setModifyUser(memerId.toString());
        dsfGmApiPrefixMapper.updateByPrimaryKey(dsfGmApiPrefix);
        return  R.ok();
    }


    public PageInfo<DsfGmApiPrefix> prefixListPage(String prefix, String siteCode, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<DsfGmApiPrefix> dsfGmApiPrefixList = dsfGmApiPrefixService.prefixList(prefix, siteCode);
        return new PageInfo<>(dsfGmApiPrefixList);
    }


    public List<DsfGmApiPrefix> prefixList(String prefix, String siteCode) {
        DsfGmApiPrefix dsfGmApiPrefix = new DsfGmApiPrefix();
        if(!StringUtils.isEmpty(prefix)){
            dsfGmApiPrefix.setPrefix(prefix);
        }
        return sysMapper.selectPrefixByPrefixOrSiteCode(prefix,siteCode);

    }
}


