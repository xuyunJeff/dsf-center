package com.xuyun.platform.dsfcenterdata.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "DsfMemberUser", description = "第三方玩家表")
@Table(name = "dsf_member")
public class DsfMemberUser implements Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "平台玩家 memberUser")
  private String memberUser;

  @ApiModelProperty(value = "第三方游戏平台id")
  private String dsfPlayerId;

  @ApiModelProperty(value = "游戏平台")
  private String platformCode;

  @ApiModelProperty(value = "站点代缩写代码")
  private String siteCode;

  @ApiModelProperty(value = "是否可用")
  private String available;

  @ApiModelProperty(value = "更新时间")
  private String updateTime;

  @ApiModelProperty(value = "开始时间")
  private String createTime;

  @ApiModelProperty("api线路名称")
  private String apiName;

  @ApiModelProperty("用户第三方密码")
  private String password;

  public DsfMemberUser (String memberUser){
    this.memberUser = memberUser ;
  }

  public DsfMemberUser (String memberUser ,String platformCode){
    this.memberUser = memberUser ;
    this.platformCode = platformCode;
  }
}
