package com.xuyun.platform.dsfcenterdata.betlog;

import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 *
 * @ClassName: BetTaskDto
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 11:03:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Data
public class BetTaskDto {

    /**
     * 拉单开始时间
     */
    private Date sTime;

    /**
     * 拉单结束时间
     */
    private Date eTime;

    /**
     * 要拉的api线路,无参数或者只传一个值
     */
    private String apiName;

    /**
     * 是否更新redis
     */
    private boolean redisUpdate;

    /**
     * 要拉的第三方游戏平台
     */
    private GamePlatform gamePlatform;

    /**
     * 任务的extraKey
     */
    private String extraKey;

    public BetTaskDto() {
        this.redisUpdate = true;
    }
}
