package com.invech.platform.dsfcenterdata.betlog;

import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: BetTaskRequestParams
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 15:46:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BetTaskRequestParams implements Cloneable{
    /**
     * 要拉单的线路
     */
    private DsfGmApi dsfGmApi;
    /**
     * 某些平台特殊的拉单key,自由发挥
     */
    private String  betExtraKey;

    /**
     * 拉单的开始时间
     */
    private Date sTime;

    /**
     * 拉单的结束时间
     */
    private Date eTime;
    /**
     * 是否更新redis
     */
    private boolean redisUpdate = true;

    /**
     * 日志ID
     * @param betExtraKey
     */
    private String id;
    public BetTaskRequestParams(String betExtraKey) {
        this.betExtraKey = betExtraKey;
    }

    @Override
    public BetTaskRequestParams clone() throws CloneNotSupportedException {
        return (BetTaskRequestParams)super.clone();
    }
}
