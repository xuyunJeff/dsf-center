package com.invech.platform.dsfcenterdata.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;

@Data
@ApiModel(value = "站点信息", description = "站点信息")
@Table(name = "dsf_site")
@ToString
public class DsfSite implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "id")
	private Integer id;

	@ApiModelProperty(value = "站点代缩写代码")
	private String siteCode;

	@ApiModelProperty(value = "站点名称")
	private String siteName;

	@ApiModelProperty(value = "分片名数据库")
	private String schemaName;

	@ApiModelProperty(value = "商户名称 ID")
	private Integer companyId;

	@ApiModelProperty(value = "开始时间")
	private String startDate;

	@ApiModelProperty(value = "结束时间")
	private String endDate;

	@ApiModelProperty(value = "状态　1开启，0禁用")
	private Byte available;

	@ApiModelProperty(value="交站时间")
	private String useTime;
	
	@ApiModelProperty(value="备注")
	private String memo;
	
	@ApiModelProperty(value="创建人")
	private String createUser;
	
	@ApiModelProperty(value="创建时间")
	private String createTime;
	
	@ApiModelProperty(value="最后一次修改时间")
	private String modifyTime;
	
	@ApiModelProperty(value="币种")
	private String currency;

	@ApiModelProperty(value="0：包网，1：API")
	private Byte isApi;

	@ApiModelProperty(value="更新人")
	private String modifyUser;

	@ApiModelProperty(value = "业主名称")
	private String companyUser;

	public DsfSite() { }

	public DsfSite(String siteCode, String siteName, String schemaName, Integer companyId, String startDate, String endDate, Byte available, String useTime, String memo, String createUser, String createTime, String modifyTime, String currency, Byte isApi, String modifyUser,String companyUser) {
		this.siteCode = siteCode;
		this.siteName = siteName;
		this.schemaName = schemaName;
		this.companyId = companyId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.available = available;
		this.useTime = useTime;
		this.memo = memo;
		this.createUser = createUser;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.currency = currency;
		this.isApi = isApi;
		this.modifyUser = modifyUser;
		this.companyUser = companyUser;
	}
}