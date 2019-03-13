package com.xuyun.platform.modules.common.constants;

import java.util.List;

/**
 * @ClassName: DsConstants
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 15:11:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class DsConstants {

    /**
     * 登录
     */
    public static final String COMMAND_LOGIN ="LOGIN";

    /**
     * 修改密码
     */
    public static final String COMMAND_CHANGE_PASSWORD ="CHANGE_PASSWORD";

    /**
     * 存款
     */
    public static final String COMMAND_DEPOSIT="DEPOSIT";

    /**
     * 取款
     */
    public static final String COMMAND_WITHDRAW="WITHDRAW";

    /**
     * 获取余额
     */
    public static final String COMMAND_GET_BALANCE="GET_BALANCE";

    /**
     * 验证交易号
     */
    public static final String COMMAND_CHECK_REF="CHECK_REF";

    /**
     * 修改注单ID拉取
     */
    public static final String COMMAND_GET_ADJUSTED_RECORD = "GET_ADJUSTED_RECORD";

    /**
     * 游戏注单拉取(新)
     */
    public static final String COMMAND_GET_RECORD_BY_SEQUENCENO="GET_RECORD_BY_SEQUENCENO";

    public static final String[] POKER=new String[]{
    "黑桃A","黑桃2","黑桃3","黑桃4","黑桃5","黑桃6","黑桃7","黑桃8","黑桃9","黑桃10","黑桃J","黑桃Q","黑桃K",
    "红桃A","红桃2","红桃3","红桃4","红桃5","红桃6","红桃7","红桃8","红桃9","红桃10","红桃J","红桃Q","红桃K",
    "梅花A","梅花2","梅花3","梅花4","梅花5","梅花6","梅花7","梅花8","梅花9","梅花10","梅花J","梅花Q","梅花K",
    "方块A","方块2","方块3","方块4","方块5","方块6","方块7","方块8","方块9","方块10","方块J","方块Q","方块K"
    };

    public static String getPoker(List<Integer> pokerNumbers){
        StringBuffer poker =new StringBuffer();
        for (int i = 0; i < pokerNumbers.size(); i++) {
            poker.append(POKER[i]+",");
        }
        return poker.substring(0,poker.length()-1);
    }

    public interface Language {
        //CN,HK,EN,TH,VN
        String cn = "CN";
        String hk = "HK";
        String en = "EN";
        String th = "TH";
        String vn = "VN";
    }
}
