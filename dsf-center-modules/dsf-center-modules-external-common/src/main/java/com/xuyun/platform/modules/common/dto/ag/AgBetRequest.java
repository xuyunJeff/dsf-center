package com.xuyun.platform.modules.common.dto.ag;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 10:44 2017/12/28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgBetRequest implements Cloneable{

    private String url;//FTP服务器hostname
    private String username;//FTP登录账号
    private String password;//FTP登录密码
    private String remotePath;//路径
    private DsfGmApi api;
    private Long lastDownloadFileName;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AgBetRequest(Object url, Object username, Object password, String remotePath, DsfGmApi api,Long lastDownloadFileName) {
        this.url = url.toString();
        this.username = username.toString();
        this.password = password.toString();
        this.remotePath = remotePath;
        this.api = api;
        this.lastDownloadFileName = lastDownloadFileName;
    }
}
