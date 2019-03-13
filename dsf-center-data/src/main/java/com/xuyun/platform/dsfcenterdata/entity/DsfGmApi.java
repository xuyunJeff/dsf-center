package com.xuyun.platform.dsfcenterdata.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xuyun.platform.dsfcenterdata.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ApiModel(value = "DsfGmApi", description = "")
@Table(name = "dsf_gm_api")
@ToString
public class DsfGmApi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "平台代码")
    private String platformCode;

    @ApiModelProperty(value = "API接口名称")
    private String apiName;

    @ApiModelProperty(value = "PC端口接口地址1")
    private String pcUrl;

    @ApiModelProperty(value = "PC端接口地址2")
    private String pcUrl2;

    @ApiModelProperty(value = "移动接口地址1")
    private String mbUrl;

    @ApiModelProperty(value = "移动接口地址2")
    private String mbUrl2;

    @ApiModelProperty(value = "代理号")
    private String agyAcc;

    @ApiModelProperty(value = "密钥")
    private String md5Key;

    @ApiModelProperty(value = "安全码格式(以JSON格式存储)")
    private String secureCode;

    @ApiModelProperty(value = "代理前缀代码")
    private String proxyFore;

    @ApiModelProperty(value = "排序号")
    private Integer sortId;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    @ApiModelProperty(value = "修改时间")
    private String modifyTime;

    @ApiModelProperty(value = "1开启，0禁用")
    private Byte available;

    @ApiModelProperty(value = "网站名称(BBIN)")
    private String webName;

    @ApiModelProperty(value = "站点代缩写代码")
    @Transient
    private String siteCode;

    @ApiModelProperty(value = "线路前缀")
    @Transient
    private String prefix;

    @ApiModelProperty(value = "站点下的线路是否可用")
    @Transient
    private String siteApiAvailable;

    public static Map<String, Object> getSecureCodeMap(String secureCode) {
        return JSONObject.parseObject(secureCode, new TypeReference<Map<String, Object>>() {
        });
    }

    public String getPcUrl() {
        if (StringUtils.isEmpty(pcUrl)) {
            return pcUrl;
        }
        return pcUrl.endsWith("/") ? pcUrl : pcUrl + "/";
    }

    public String getPcUrl2() {
        if (StringUtils.isEmpty(pcUrl2)) {
            return pcUrl2;
        }
        return pcUrl2.endsWith("/") ? pcUrl2 : pcUrl2 + "/";
    }

    public String getMbUrl() {
        if (StringUtils.isEmpty(mbUrl)) {
            return mbUrl;
        }
        return mbUrl.endsWith("/") ? mbUrl : mbUrl + "/";
    }

    public String getMbUrl2() {
        if (StringUtils.isEmpty(mbUrl2)) {
            return mbUrl2;
        }
        return mbUrl2.endsWith("/") ? mbUrl2 : mbUrl2 + "/";
    }

}