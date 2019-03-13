package com.xuyun.platform.dsfcenterdata.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  TransferType {
  /**
   * 中心到第三方
   */
  TopUp("中心到第三方",1,"TopUp"),
  /**
   * 第三方到中心
   */
  Withdraw("第三方到中心",2,"Withdraw");

  private String name;
  private Integer code;
  private String type;

  // 构造方法
  private TransferType(String name, int code) {
    this.name = name;
    this.code = code;
  }

  // 普通方法
  public static String getName(int code) {
    for (TransferType c : TransferType.values()) {
      if (c.getCode() == code) {
        return c.name;
      }
    }
    return null;
  }

  public static Integer getCode(String name) {
    for (TransferType c : TransferType.values()) {
      if (c.getName().equals(name)) {
        return c.code;
      }
    }
    return null;
  }
}
