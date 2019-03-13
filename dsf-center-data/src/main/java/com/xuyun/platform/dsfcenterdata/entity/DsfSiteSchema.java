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

@Data
@ApiModel(value = "站点映射信息", description = "站点映射信息")
@Table(name = "dsf_site_schema")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DsfSiteSchema implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "站点 ID")
    private Integer siteId;
    @ApiModelProperty(value = "schema ID")
    private Integer schemaId;
    @ApiModelProperty(value = "创建人")
    private String createUser;
    @ApiModelProperty(value = "更新人")
    private String modifyUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "最后一次修改时间")
    private String modifyTime;

    @ApiModelProperty(value = "是否有效 1有效 0无效")
    private Byte avaliable;


}
