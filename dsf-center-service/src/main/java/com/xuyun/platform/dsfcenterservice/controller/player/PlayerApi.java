package com.xuyun.platform.dsfcenterservice.controller.player;

import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.dto.LaunchGameDto;
import com.xuyun.platform.dsfcenterdata.dto.MemberBalanceDto;
import com.xuyun.platform.dsfcenterdata.dto.WithdrawBalanceAll;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.Terminal;
import com.xuyun.platform.dsfcenterdata.enums.TransferType;
import com.xuyun.platform.dsfcenterdata.response.R;
import com.xuyun.platform.dsfcenterdata.response.RedisPageHelper;
import com.xuyun.platform.dsfcenterservice.controller.BaseController;
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

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/api/player")
@Api(value = "player", description = "客户端功能")

public class PlayerApi extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    TransferService transferService;


    @PostMapping("createDsfMember")
    @ApiOperation(tags = {"player"}, value = "创建第三方玩家")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R createDsfMember(@NotBlank(message = "缺少参数gamePlatform") @RequestParam("gamePlatform") GamePlatform gamePlatform) {
        DsfMemberUser dsfMemberUser = userService.checkDsfMemberUserExists(super.getMemberUser(), gamePlatform);
        return R.ok().put(dsfMemberUser);
    }


    @PostMapping("transfer")
    @ApiOperation(tags = {"player"}, value = "转账")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R transfer(@NotBlank(message = "缺少参数gamePlatform") @RequestParam("gamePlatform") GamePlatform gamePlatform,
                      @NotBlank(message = "缺少参数transferType") @RequestParam("transferType") TransferType transferType,
                      @NotBlank(message = "缺少参数amount") @RequestParam("amount") BigDecimal amount, @RequestParam("transactionId") String transactionId) {
        if (!checkMemberAvailable(gamePlatform)) {
            return R.error(ErrorCode.DSF_MEMBER_LOCK_API, "platform = " + gamePlatform);
        }
        if (!checkGamePlatformApiAvailable(gamePlatform)) {
            return R.error(ErrorCode.DSF_GAME_API_NOTAVAILABLE, "platform = " + gamePlatform);
        }
        if (gamePlatform == GamePlatform.Ds) {
            checkRequestLimit(gamePlatform, "startGame");
        }
        DsfTransferLog transferLog = transferService.performTransfer(gamePlatform, transferType, amount, transactionId, super.getMemberUser(), super.getMemberUser());
        return R.ok().put(transferLog);
    }

    @PostMapping("allBalanceTransferToCenter")
    @ApiOperation(tags = {"player"}, value = "一键回归余额")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R allBalanceTransferToCenter(@RequestBody List<WithdrawBalanceAll> transferList) {
        List<DsfTransferLog> transferLogs = transferService.transferBalanceBackAll(transferList, super.getMemberUser(), super.getMemberUser());
        return R.ok().put(transferLogs);
    }

    @PostMapping("startGame")
    @ApiOperation(tags = {"player"}, value = "开始游戏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R startGame(@NotBlank(message = "缺少参数gamePlatform") @RequestParam("gamePlatform") GamePlatform gamePlatform,
                       @NotBlank(message = "缺少参数gameCode") @RequestParam("gameCode") String gameCode,
                       @NotBlank(message = "缺少参数terminal") @RequestParam(value = "terminal") Terminal terminal,
                       @NotBlank(message = "缺少参数homeUrl") @RequestParam(value = "homeUrl") String homeUrl) {
        // 开始游戏
        if (!checkMemberAvailable(gamePlatform)) {
            return R.error(ErrorCode.DSF_MEMBER_LOCK_API, "platform = " + gamePlatform);
        }
        if (!checkGamePlatformApiAvailable(gamePlatform)) {
            return R.error(ErrorCode.DSF_GAME_API_NOTAVAILABLE, "platform = " + gamePlatform);
        }
        if (gamePlatform == GamePlatform.Ds) {
            checkRequestLimit(gamePlatform, "startGame");
        }
        LaunchGameDto launchGameDto = gameService.launchGame(gamePlatform, super.getMemberUser(), gameCode, terminal, homeUrl);
        return R.ok().put(launchGameDto);
    }

    @PostMapping("getDsfBalance")
    @ApiOperation(tags = {"player"}, value = "获得第三方余额")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R getDsfBalance(@NotBlank(message = "缺少参数gamePlatform") @RequestParam("gamePlatform") GamePlatform gamePlatform) {
        if (!checkGamePlatformApiAvailable(gamePlatform)) {
            return R.error(ErrorCode.DSF_GAME_API_NOTAVAILABLE, "platform = " + gamePlatform);
        }
        MemberBalanceDto balance = gameService.memberBalance(gamePlatform, super.getMemberUser());
        return R.ok().put(balance);
    }

    @PostMapping("getDsfAllBalance")
    @ApiOperation(tags = {"player"}, value = "获得所有第三方平台余额")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R getDsfAllBalance() {
        for (GamePlatform gamePlatform : GamePlatform.values()) {
            if (!checkGamePlatformApiAvailable(gamePlatform)) {
                return R.error(ErrorCode.DSF_GAME_API_NOTAVAILABLE, "platform = " + gamePlatform);
            }
        }
        List<MemberBalanceDto> memberBalanceDtos = gameService.memberBalanceAll(super.getMemberUser());
        return R.ok().put(memberBalanceDtos);
    }

    @PostMapping("gameList")
    @ApiOperation(tags = {"player"}, value = "获取游戏列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R gameList(@NotBlank(message = "缺少参数gamePlatform") @RequestParam("gamePlatform") GamePlatform gamePlatform,
                      @NotBlank(message = "缺少参数pageNo") @RequestParam("pageNo") Integer pageNo,
                      @NotBlank(message = "缺少参数pageSize") @RequestParam("pageSize") Integer pageSize) {
        if (pageSize <= 0) {
            return R.error("pageSize 必须大于0");
        }
        RedisPageHelper gmGameList = gameService.gameListPage(gamePlatform, siteUtil.getSiteCode(), pageNo, pageSize);
        return R.ok().put(gmGameList);
    }

    @PostMapping("cheackTransferState")
    @ApiOperation(tags = {"player"}, value = "检查第三方转账状态")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = ApiConstants.SITE_SECURETY_KEY, value = "Site 站点", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = ApiConstants.PLAYER_LOGGIN_KEY, value = "登录玩家的信息", required = true, dataType = "String", paramType = "header")})
    public R cheackTransferState(@NotBlank(message = "缺少参数transactionId") @RequestParam("transactionId") String transactionId) {
        DsfTransferLog dsfTransferLog = transferService.checkTransferStatus(transactionId);
        return R.ok().put(dsfTransferLog);
    }
}
