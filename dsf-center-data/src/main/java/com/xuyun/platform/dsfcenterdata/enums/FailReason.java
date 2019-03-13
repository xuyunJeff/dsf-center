package com.xuyun.platform.dsfcenterdata.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FailReason {

  BalanceNotAlailable("第三方余额不足"),

  TranstaitonExist("订单号存在"),

  PlayerAbnormable("玩家状态异常"),

  DsfBalanceUpdateFailed("第三方余额更新失败,或其他失败"),

  ApiServerMaintaining("第三方维护中"),

  TranstaitonNotExist("第三方游戏平台订单号不存在");

  public String message;
}
