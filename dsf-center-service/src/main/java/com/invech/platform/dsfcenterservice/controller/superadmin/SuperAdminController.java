package com.invech.platform.dsfcenterservice.controller.superadmin;

import com.github.pagehelper.PageInfo;
import com.invech.platform.dsfcenterdao.service.DsfGmApiPrefixService;
import com.invech.platform.dsfcenterdao.service.DsfGmPlatformService;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmApiPrefix;
import com.invech.platform.dsfcenterdata.entity.DsfGmPlatform;
import com.invech.platform.dsfcenterdata.entity.DsfSite;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.dsfcenterservice.controller.BaseController;
import com.invech.platform.dsfcenterservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/su-admin")
@Api(value = "su-admin", description = "站点有关")
public class SuperAdminController extends BaseController {

    @Autowired
    private DsfGmPlatformService dsfGmPlatformService;

    @Autowired
    private UserService userService;

    @Autowired
    private DsfSiteService dsfSiteService;

    @Autowired
    DsfGmApiPrefixService dsfGmApiPrefixService;

    @PostMapping("addDsfGamePlatform")
    @ApiOperation(tags = {"su-admin"}, value = "增加某个游戏平台")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R addDsfGamePlatform(@RequestBody DsfGmPlatform dsfGmPlatform) {

        if (dsfGmPlatform == null) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        } else {
            R result = dsfGmPlatformService.savePlatform(dsfGmPlatform, userService.getMemberUser());
            return result;
        }
    }

    @PostMapping("addDsfGamePlatformLine")
    @ApiOperation(tags = {"su-admin"}, value = "增加某个游戏平台的某一条线路")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R addDsfGamePlatformLine(@RequestParam("platformCode") String platformCode, @RequestParam("prefix") String prefix, @RequestParam("siteCode") String siteCode) {
        if (StringUtils.isEmpty(platformCode) || StringUtils.isEmpty(prefix) || StringUtils.isEmpty(siteCode)) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        } else {
           R result = dsfGmPlatformService.savePlatformPrefix(platformCode, prefix, siteCode,userService.getMemberUser());
           return result;
        }

    }

    @PostMapping("addSite")
    @ApiOperation(tags = {"su-admin"}, value = "增加一个站点")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R addSite(@RequestParam("schemaId") Integer schemaId, @RequestBody DsfSite dsfSite) {
        if (dsfSite == null) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return dsfSiteService.addSiteCode(dsfSite, userService.getMemberUser(), schemaId);
    }

    @PostMapping("freezeSite")
    @ApiOperation(tags = {"su-admin"}, value = "冻结某个站点")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R freezeSite(@RequestParam("siteId") Integer siteId,@RequestParam("avaliable") Boolean avaliable) {
        if (siteId == 0) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(),ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        R result = dsfSiteService.enabledSite(siteId,avaliable, userService.getMemberUser(), siteUtil.getSiteCode());
        return result;
    }

    @PostMapping("enableOrDisablePlatPrefix")
    @ApiOperation(tags = {"su-admin"}, value = "开启/停用平台线路")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点对应加密串", required = true, dataType = "Integer", paramType = "header") ,
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R enableOrDisablePlatPrefix(@RequestParam("prefixId") Integer prefixId, @RequestParam("avaliable") Boolean avaliable){
        if(prefixId == null){
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return dsfGmApiPrefixService.enableOrDisEnablePrefix(prefixId,avaliable,userService.getMemberUser());
    }

    @PostMapping("platPrefixList")
    @ApiOperation(tags = {"su-admin"}, value = "获取线路列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R platPrefixList(@RequestParam(required = false) String prefix, @RequestParam(required = false) String siteCode, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        PageInfo<DsfGmApiPrefix> prefixList = dsfGmApiPrefixService.prefixListPage(prefix, siteCode,pageNo, pageSize);
        return R.ok().put(prefixList);
    }


    @PostMapping("siteStatements")
    @ApiOperation(tags = {"su-admin"}, value = "获取站点的运营报表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R siteStatements() {
        return R.ok();
    }
}
