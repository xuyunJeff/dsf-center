package com.invech.platform.dsfcenterservice.controller.site;

import com.github.pagehelper.PageInfo;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfSite;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.response.R;
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
@RequestMapping("/api/site")
@Api(value = "SiteCode", description = "站点有关")
public class SiteCodeController extends BaseController {

    @Autowired
    private DsfSiteService dsfSiteService;

    @Autowired
    private UserService userService;

    @PostMapping("/getSchemaName")
    @ApiOperation(tags = {"site"}, value = "获取当前站点对应的数据库加密信息的解密信息", notes = "获取当前站点对应的数据库加密信息的解密信息")
    @ApiImplicitParams({@ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "token头部，随便填数字", required = true, dataType = "Integer", paramType = "header")})
    @ResponseBody
    public R getSchemaName() {
        // 域名对应多个站点,获取站点,再通过站点获取数据库前缀
        String schemaName = siteUtil.getSchemaName();
        return R.ok().put(schemaName);
    }


    @PostMapping("siteList")
    @ApiOperation(tags = {"site"}, value = "获取站点列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R siteList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam(required = false) String siteName,@RequestParam(required = false) String siteCode) {
            PageInfo siteList = dsfSiteService.getSitesList(pageNo, pageSize, siteName,siteCode);
            return R.ok().put(siteList);
    }


    @PostMapping("addSite")
    @ApiOperation(tags = {"site"}, value = "新增站点")
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


    @PostMapping("editSite")
    @ApiOperation(tags = {"site"}, value = "修改站点")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R editSite(@RequestBody DsfSite dsfSite) {
        if (dsfSite == null) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return dsfSiteService.editSite(dsfSite, userService.getMemberUser());
    }


    @PostMapping("deleteSite")
    @ApiOperation(tags = {"site"}, value = "删除站点")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R deleteSite(@RequestParam("siteId") Integer siteId) {
        if (siteId == 0) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return dsfSiteService.deleteSite(siteId,siteUtil.getSiteCode());
    }


    @PostMapping("enableSite")
    @ApiOperation(tags = {"site"}, value = "开启/关闭站点")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R enableGame(@RequestParam("siteId") Integer siteId, @RequestParam("available") Boolean available) {
        if (siteId == 0) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return dsfSiteService.enabledSite(siteId,available,userService.getMemberUser(),siteUtil.getSiteCode());
    }


}
