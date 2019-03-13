package com.xuyun.platform.dsfcenterdata.entity;

import com.xuyun.platform.dsfcenterdata.enums.TransferState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DsfTransferLog", description = "转账记录")
@Table(name = "dsf_transfer_log")
public class DsfTransferLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "transactionId")
  private String transactionId;

  @ApiModelProperty(value = "memberUser")
  private String memberUser;

  @ApiModelProperty(value = "站点名")
  private String siteCode;

  @ApiModelProperty(value = "玩家帐号")
  private String dsfPlayerId ;

  @ApiModelProperty(value = "游戏平台")
  private String platformCode;

  @ApiModelProperty(value = "转账类型(充值 or 取款)")
  private String type ;

  @ApiModelProperty(value = "转账状态")
  private String state ;

  @ApiModelProperty(value = "失败原因 当state == failed 时 该状态有值")
  private String failReason;

  @ApiModelProperty(value = "第三方订单Id")
  private String dsfTransactionId;

  @ApiModelProperty(value = "转账金额")
  private BigDecimal amount;

  @ApiModelProperty(value = "余额变更事件Id")
  private Long balanceChangedId;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "完成时间")
  private LocalDateTime completeTime;

  @ApiModelProperty(value = "版本号")
  private Integer version ;

  @ApiModelProperty(value = "转账请求线路名称")
  private String apiName;

  @ApiModelProperty(value = "操作者id")
  private String operator;

  public TransferState getTransferState(){
    return TransferState.getTransferState(this.state);
  }

  public DsfTransferLog(String transactionId, String memberUser, String siteCode, String dsfPlayerId, String platformCode, String type, String state, String failReason, String dsfTransactionId, BigDecimal amount, Long balanceChangedId, LocalDateTime createTime, LocalDateTime completeTime, Integer version, String apiName, String operator) {
    this.transactionId = transactionId;
    this.memberUser = memberUser;
    this.siteCode = siteCode;
    this.dsfPlayerId = dsfPlayerId;
    this.platformCode = platformCode;
    this.type = type;
    this.state = state;
    this.failReason = failReason;
    this.dsfTransactionId = dsfTransactionId;
    this.amount = amount;
    this.balanceChangedId = balanceChangedId;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.version = version;
    this.apiName = apiName;
    this.operator = operator;
  }

  public DsfTransferLog(String transactionId) {
    this.transactionId = transactionId;
  }
}
