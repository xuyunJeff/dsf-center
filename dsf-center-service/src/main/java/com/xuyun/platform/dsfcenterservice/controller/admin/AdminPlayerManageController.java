package com.xuyun.platform.dsfcenterservice.controller.admin;

import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.dto.MemberBalanceDto;
import com.xuyun.platform.dsfcenterdata.dto.WithdrawBalanceAll;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.response.R;
import com.xuyun.platform.dsfcenterdata.utils.StringUtils;
import com.xuyun.platform.dsfcenterservice.controller.BaseController;
import com.xuyun.platform.dsfcenterservice.service.AdminManageGamesService;
import com.xuyun.platform.dsfcenterservice.service.GameService;
import com.xuyun.platform.dsfcenterservice.service.TransferService;
import com.xuyun.platform.dsfcenterservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 玩家行为管控
 */
@Api(value = "Agmin-Players", description = "玩家管理")
@RequestMapping("/api/admin")
@Controller
public class AdminPlayerManageController extends BaseController {

    @Autowired
    AdminManageGamesService adminManageGamesService;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    TransferService transferService;

    /**
     * 玩家转账
     *
     * @return
     */
    @PostMapping("playerBalanceBack")
    @ApiOperation(tags = {"Admin"}, value = "回收玩家某个平台的余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R playerBalanceBack(@RequestParam("player") String player, WithdrawBalanceAll transferList) {
        DsfTransferLog transferLog = transferService.transferBalanceBack(transferList, player, super.getMemberUser());
        return R.ok().put(transferLog);
    }

    /**
     * 玩家全部金额转到中心
     *
     * @return
     */
    @PostMapping("playerAllBalanceBack")
    @ApiOperation(tags = {"Admin"}, value = "玩家余额回归中心")
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R playerAllBalanceBack(@RequestBody List<WithdrawBalanceAll> transferList, @RequestParam("player") String player) {
        List<DsfTransferLog> transferLogs = transferService.transferBalanceBackAll(transferList, player, super.getMemberUser());
        return R.ok().put(transferLogs);
    }

    /**
     * 冻结玩家
     *
     * @return
     */
    @PostMapping("freezePLayer")
    @ApiOperation(tags = {"Admin"}, value = "解封/冻结玩家")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R freezePLayer(@RequestParam("memberUser") String memberUser, @RequestParam("avaliable") Boolean avaliable) {
        if (StringUtils.isEmpty(memberUser)) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        adminManageGamesService.freezeUser(memberUser, avaliable);
        return R.ok();
    }

    /**
     * 冻结玩家某个平台不能玩
     *
     * @return
     */
    @PostMapping("freezePLayerGamePlatform")
    @ApiOperation(tags = {"Admin"}, value = "冻结玩家某个平台不能玩")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R freezePLayerGamePlatform(@RequestParam("gamePlatform") GamePlatform gamePlatform, @RequestParam("memberUser") String memberUser) {
        if (StringUtils.isEmpty(gamePlatform.platformCode) || memberUser == null) {
            return R.error(ErrorCode.DSF_PARAM_NOT_NULL.getCode(), ErrorCode.DSF_PARAM_NOT_NULL.getMsg());
        }
        return adminManageGamesService.freezeUserByPlatform(gamePlatform.platformCode, memberUser);
    }

    /**
     * 查看玩家第三方余额
     *
     * @return
     */
    @PostMapping("playerBalance")
    @ApiOperation(tags = {"Admin"}, value = "查看玩家第三方余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R playerBalance(@RequestParam("player") String player) {
        List<MemberBalanceDto> memberBalanceDtos = gameService.memberBalanceAll(player);
        return R.ok().put(memberBalanceDtos);
    }
}
