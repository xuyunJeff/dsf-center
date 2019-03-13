package com.invech.platform.dsfcenterdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author R.M.I
 */
@AllArgsConstructor
@Getter
public enum TransferState {

  NotExist(null,"不存在","NotExist"),
  /**
   * 转账初始化
   */
  Init(TransferStateType.HANDLING, "初始化","Init"),


  // 未知
  // 1. 充值： 调用dsf进行校验
  // 2. 取款：调用balance进行校验
  UnKnow(TransferStateType.HANDLING, "未知","UnKnow"),

  // 成功
  Successful(TransferStateType.COMPLETED, "成功","Successful"),

  // 失败
  Failed(TransferStateType.FAILED, "失败","Failed");



  // 客户端显示状态
  public TransferStateType clientState;

  // 中文显示
  public String message;

  // 数据库显示名
  public String code;

  public static TransferState getTransferState(String code){
    for (TransferState state : TransferState.values()) {
      if(state.code.equals(code)){
        return state;
      }
    }
    return null;
  }

}
