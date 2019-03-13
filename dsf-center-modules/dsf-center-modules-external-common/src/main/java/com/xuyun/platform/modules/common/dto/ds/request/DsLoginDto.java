package com.xuyun.platform.modules.common.dto.ds.request;

import com.xuyun.platform.modules.common.constants.DsConstants;
import lombok.Data;

import java.util.Random;

/**
 * @ClassName: DsLoginDto
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 15:05:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class DsLoginDto {
    /*
    •　username: 合作伙伴平台的用户ID,字符串长度少于或者等于20位, 由字母和数字组成
    •　password: 合作伙伴平台的用户原始密码，要求ＭＤ５加密, 长度为32位
    •　currency:  合作伙伴的用户ID所使用的币别
    •　nickname:  合作伙伴的用户昵称, 字符串长度少于或者等于20位
    •　language:  游戏语言(CN,HK,EN,TH,VN)
    •　line:  线路(可选参数) 　参数(1, 2, …)  默认线路为1, 注:具体线路参数ID以游戏平台为准，其中真人娱乐https线路参数line为3、4、5、6、7、8，具体线路以具体后台设置为准
    •　homeUrl:  游戏返回或退出时的回调地址，一般为客户主页面地址或者客户游戏大厅地址。
     */

    private String username;
    private String password;
    private String currency = "CNY";
    private String nickname;
    private String language= DsConstants.Language.cn;
    private Integer line;
    private String homeUrl;

    public static int generateDsLoginLine(){
        int[] line ={3,4,5,6,7,8};
        Random rand=new Random();
        int j=rand.nextInt(5);
        return line[j];
    }
}
