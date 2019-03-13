package com.invech.platform.dsfcenterservice.controller.admin;


import com.invech.platform.dsfcenterdao.service.DsfBetlogService;
import com.invech.platform.dsfcenterdao.service.DsfGmPlatformService;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmPlatform;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.response.RRException;

import com.invech.platform.dsfcenterservice.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;


/**
 * @author R.M.I
 */
@Controller
@RequestMapping("/api/admin")
@Api(value = "Agmin-Platform", description = "游戏平台管理")
public class AdminGamePlatFormController extends BaseController {

    @Autowired
    private DsfGmPlatformService dsfGmPlatformService;

    @Autowired
    DsfBetlogService dsfBetlogService;

    @PostMapping("platFormList")
    @ApiOperation(tags = {"Admin"}, value = "某个站点获取已开通的的游戏平台")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R gameList(@RequestParam(required = false) GamePlatform gamePlatform) {
        List<DsfGmPlatform> gmPlatformList = dsfGmPlatformService.platformList(gamePlatform,siteUtil.getSiteCode());
        return R.ok().put(gmPlatformList);
    }


    @PostMapping("betlogs")
    @ApiOperation(tags = {"Admin"}, value = "批量获取注单接口,限制按site不同,游戏类型不同,10秒一次,size 最大2000条,第一次拉单默认为10000000000000000")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header")})
    public R betlogs(@NotBlank @RequestParam(name = "platformType") PlatformType platformType ,@NotBlank @RequestParam(name = "lastUpadteId")  String lastUpadteId,
                     @NotBlank @RequestParam(name = "size") Integer size) {
        if(size > 2000){
            throw new RRException(ErrorCode.DSF_GAME_API_NOTAVAILABLE);
        }
        if(lastUpadteId.length() != 17){
            return R.error(500,"lastUpadteId 长度错误");
        }
        checkBetlogLimit(platformType,siteUtil.getSiteCode());

        switch (platformType){
            case VideoGame:
               return R.ok().put(dsfBetlogService.videoGameBetlogsBatch(size,lastUpadteId,siteUtil.getSiteCode()));
            case Poker:
                return R.ok().put(dsfBetlogService.pokerBetlogsBatch(size,lastUpadteId,siteUtil.getSiteCode()));
            case Fish:
                return R.ok().put(dsfBetlogService.fishingBetlogsBatch(size,lastUpadteId,siteUtil.getSiteCode()));
            case ComputerGame:
                return R.ok().put(dsfBetlogService.computerGameBetlogsBatch(size,lastUpadteId,siteUtil.getSiteCode()));
        }
        return R.ok().put(Collections.EMPTY_LIST);
    }


}
