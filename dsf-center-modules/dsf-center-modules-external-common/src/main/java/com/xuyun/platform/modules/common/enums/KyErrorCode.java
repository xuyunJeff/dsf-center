package com.xuyun.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: KyErrorCode
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 19:15:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum  KyErrorCode {

    Success(0,"成功"),
    InValidToken(1,"TOKEN 丢失（重新调用登录接口获取）"),
    InValidAgent(2,"渠道不存在（请检查渠道 ID 是否正确）"),
    TimeStampTimeOut(3,"验证时间超时（请检查 timestamp 是否正确）"),
    ChexkEror(4,"验证错误"),
    WhiteIp(5,"渠道白名单错误（请联系客服添加服务器白名单）"),
    ParamError(6,"验证字段丢失（请检查参数完整性）"),
    MethodNotExist(8,"不存在的请求（请检查子操作类型是否正确）"),
    Md5Error(15,"渠道验证错误（1.MD5key 值是否正确；2.生成 key 值中的 timestamp 与参数中的是否一致；3. 生成 key 值中的 timestamp 与代理编号以字符串形式拼接）"),
    DataNotExist(16," 数据不存在（当前没有注单）"),
    AccountLock(20," 账号禁用"),
    AESError(22," AES 解密失败"),
    BetlogTimeOut(24," 渠道拉取数据超过时间范围"),
    TranstaitonNotExist(26," 订单号不存在"),
    DataAbNormable(27," 数据库异常"),
    IpLock(28," ip 禁用"),
    TranstaitonRuleError(29," 订单号与订单规则不符"),
    PlayerStateNotAvailable(30," 获取玩家在线状态失败"),
    TransferError(31," 更新的分数小于或者等于0"),
    updatePlayerError(32," 更新玩家信息失败"),
    updatePlayerBalanceError(33," 更新玩家金币失败"),
    TranstaitonExist(34," 订单重复"),
    PlayerNotExist(35," 获取玩家信息失败（请调用登录接口创建账号）"),
    BalanceNotAvailable(38," 余额不足导致下分失败");

    private Integer code;

    private String msg;

    public static KyErrorCode kyError(Integer code){
        for(KyErrorCode dsErroeCodes : KyErrorCode.values()){
            if(dsErroeCodes.code.equals(code)){
                return dsErroeCodes;
            }
        }
        return null;
    }
}
