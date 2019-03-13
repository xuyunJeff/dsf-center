package com.xuyun.platform.dsfcenterdata.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    //系统
    SERVER_ERROR(500, "服务器异常"),

    //业务
    DSF_PARAM_NOT_NULL(20100, "传参不能为空"),
    DSF_NOT_SEARCH_GAME_INFO(20101, "未查询到该条游戏信息"),
    DSF_PLAT_FORM_REPEAT(20102, "您输入的平台名称已经存在，请重新定义"),
    DSF_NOT_SEARCH_PLAT_INFO(20103, "未查询到该平台数据信息"),
    DSF_NOT_API_PLAT(20104, "该平台还没有API信息"),
    DSF_NOT_SITE_PLAT(20105, "该平台还没有关联站点"),
    DSF_PREFIX_EXIT(20106, "该站点后缀名已存在，请重新输入"),
    DSF_NOT_SEARCH__LINE(20107, "未查询到您输入的线路"),
    DSF_NOT_FOUND_MEMBER(20108, "未查询到该平台的玩家信息"),
    DSF_PREFIX_LONG(20109, "站点前缀过长"),
    DSF_NOT_PRE_SITE(20110, "已无预备站点，请联系客服"),
    DSF_SITE_PREFIX_USED(20111, "站点前缀已被使用"),
    DSF_NOT_FOUND_SITE(20112, "未查询到该站点信息"),
    DSF_PARAM_TOO_LONG(20113, "币种长度不能大于4"),
    DSF_SITE_CODE_CANNOT_NULL(20114, "平台code不能为空"),
    DSF_EXSIT_SITE_CODE(20115, "站点code已存在，请重新输入"),
    DSF_GAME_API_NOTAVAILABLE(20116,"当前线路维护中"),
    DSF_MEMBER_LOCK_API(20117,"当前玩家不可进行此游戏平台下的游戏"),
    DSF_MEMBER_LOCK(20118,"当前玩家不可进行游戏"),
    DSF_START_GAME_CODE_ERROR(20119,"gamePlatform 和 gameCode对应错误"),
    DSF_TRANSACTION_NOT_EXIST(20120,"转账无该订单号"),
    DSF_REQUEST_LIMIT(20121,"第三方请求超出限制,限制为3秒一次"),
    DSF_TRANSACTION_EXIST(20122,"该订单号已经存在,请调用checkstate接口"),
    DSF_BET_LOG_REQUEST_LIMIT(20123,"注单获取过于频繁,按平台10秒一次"),
    DSF_BET_LOG_SIZE_LIMIT(20124,"批量获取注单最大一次2000条"),
    DSF_BALANCE_FAILED(20125,"第三方余额获取失败"),
    DSF_GAME_PLATFORM_REQUEST_ERROR(30000,"第三方http请求异常,请求失败"),
    DSF_GAME_PLATFORM_REQUEST_ERROR_AG(30001,"第三方请求异常AG,请求失败"),
    DSF_GAME_PLATFORM_REQUEST_ERROR_Ds(30002,"第三方请求异常Ds,请求失败"),
    DSF_GAME_PLATFORM_PARAM_ERROR_Ds(30003,"Ds参数转换异常"),
    DSF_GAME_PLATFORM_REQUEST_ERROR_FG(30004,"第三方请求异常Fg,请求失败"),
    DSF_GAME_PLATFORM_PARAM_ERROR_FG(30005,"Fg参数转换异常"),
    DSF_GAME_PLATFORM_REQUEST_ERROR_KY(30006,"第三方请求异常Ky,请求失败"),
    DSF_GAME_PLATFORM_PARAM_ERROR_KY(30007,"Ky参数转换异常");


    private final Integer code;
    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
