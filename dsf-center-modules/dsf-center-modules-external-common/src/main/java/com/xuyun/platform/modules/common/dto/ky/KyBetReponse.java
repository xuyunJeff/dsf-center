package com.xuyun.platform.modules.common.dto.ky;

import lombok.Data;

/**
 * @ClassName: KyLauchGameReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 19:39:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class KyBetReponse extends KyReponseD {
    //返回列表行数
    private int  count;
    //数据拉取开始时间
    private String serverStartTime;
    //数据拉取结束时间
    private String serverEndTime;

}
