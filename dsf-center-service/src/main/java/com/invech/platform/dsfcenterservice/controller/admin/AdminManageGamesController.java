package com.invech.platform.dsfcenterservice.controller.admin;


import com.invech.platform.dsfcenterdao.service.DsfGmGameService;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.response.RedisPageHelper;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.dsfcenterservice.controller.BaseController;
import com.invech.platform.dsfcenterservice.service.AdminManageGamesService;
import com.invech.platform.dsfcenterservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @author R.M.I
 */
@Controller
@RequestMapping("/api/admin")
@Api(value = "Agmin-Games", description = "游戏管理")
public class AdminManageGamesController extends BaseController {

    @Autowired
    private DsfGmGameService tGmGameService;
    @Autowired
    private AdminManageGamesService adminManageGamesService;

    @Autowired
    private UserService userService;

    @PostMapping("gameList")
    @ApiOperation(tags = {"Admin"}, value = "获取游戏列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R gameList(@RequestParam(required = false) GamePlatform gamePlatform, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        RedisPageHelper gmGameList = adminManageGamesService.gameListPage(gamePlatform, siteUtil.getSiteCode(), pageNo, pageSize);
        return R.ok().put(gmGameList);
    }

    @PostMapping("enableGame")
    @ApiOperation(tags = {"Admin"}, value = "开启/停用游戏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点对应加密串", required = true, dataType = "Integer", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R enableOrDisableGame( @RequestParam("gameId") Integer gameId, @RequestParam("available") Boolean available) {
        if (gameId == 0) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return tGmGameService.enableOrDisableGame(gameId, available, userService.getMemberUser());
    }

    @PostMapping("editGame")
    @ApiOperation(tags = {"Admin"}, value = "修改游戏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R editGame(@RequestParam("gameId") Integer gameId,@RequestParam(required = false) Integer sortId,@RequestParam(required = false) Boolean enableNew,
                      @RequestParam(required = false) Boolean enableHot,@RequestParam(required = false) Boolean available) {
        if (gameId == 0) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        } else {
            return tGmGameService.updateGame(gameId,sortId,enableHot,enableNew,available, userService.getMemberUser());
        }

    }

    @PostMapping("changeGamePicture")
    @ApiOperation(tags = {"Admin"}, value = "改变游戏图片")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R changeGamePicture(@RequestParam("gameId") String gameId, @RequestParam(required = false) String pcLogo, @RequestParam(required = false) String phoneLogo) {
        if (StringUtils.isEmpty(gameId)) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return tGmGameService.updateGameImgUrl(gameId, pcLogo, phoneLogo, userService.getMemberUser());
    }

}
