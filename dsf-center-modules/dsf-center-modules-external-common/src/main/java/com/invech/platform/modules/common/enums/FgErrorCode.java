package com.invech.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: FgErrorCode
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 16:04:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum  FgErrorCode {
    Success(0,"成功"),
    BadRequest(100,"错误请求, MERCHANTNAME&merchantcode 为空"),
    AgentInsufficient(101,"代理商余额不足"),
    InvalidMerchantNameOrCode(102,"Merchantname 账号或密码不对，或者账号被锁"),
    UnauthorizedCrossMerchant(103,"未授权，不能查询其他运营"),
    PlayerDoesNotExist(104,"该玩家不存在"),
    PlayerAlreadyExists (105,"玩家已经存在"),
    TheAccountIsFrozen (106,"账户被冻结"),
    InvalidMemberCode (107,"玩家代码必须是 5-32 个字符，并没有特殊的字符之间"),
    InvalidPassword (108,"密码应是 5-32 个字母数字"),
    IllegalChipsParameters (109,"筹码值非法"),
    IllegalOfParameters (110,"参数非法"),
    PlayerBalance(111,"玩家余额不足"),
    InvalidGameCode (112,"无效的游戏代码"),
    InvalidAccessGame (113,"该游戏不在代理商选中游戏列中 禁止访问"),
    InvalidPassword2 (114,"无效的密码值"),
    IPAddress (115,"IP 被阻止"),
    ReqeustBlocked(116,"代理商请求过多 被阻止"),
    NotWithdraw(117,"玩家正在结算无法提现"),
    SwitchGame(118,"正在赌注中無法切換遊戲"),
    NotEexist(119,"单号不存在或者该注单失败"),
    TransactionidExisted(120,"单号已经存在"),
    TimeRangeError(121,"时间范围有误"),
    InternalErrorApi(201,"内部错误"),
    PleaseTryAgain(202,"重试"),
    ChipsUpdateFail(203,"筹码更新失败"),
    GetDataFail(204,"采集数据失败"),
    PlayerAbnormalOnlineStatus(205,"玩家登录状态异常"),
    RequestTimeout(206,"超时"),
    ApiServerMaintaining(207,"维护中"),
    TransferFailed(207,"维护中");

    private Integer code;
    private String msg;

    public static String getMsgByCode(Integer code){
        for(FgErrorCode fgErrorCode : FgErrorCode.values()){
            if(code.equals(fgErrorCode.code)){
                return fgErrorCode.msg;
            }
        }
        return null;
    }
}
