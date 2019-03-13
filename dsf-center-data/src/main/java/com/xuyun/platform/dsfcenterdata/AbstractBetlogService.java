package com.xuyun.platform.dsfcenterdata;

import com.xuyun.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * @ClassName: AbstractBetlogService
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 15:41:00
 * @Description: TODO
 * @Version 1.0.0
 */
public abstract class AbstractBetlogService {

    public DsfBetlogRedisDto videoGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        throw new NotImplementedException();
    }

    public DsfBetlogRedisDto pokerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        throw new NotImplementedException();
    }

    public DsfBetlogRedisDto computerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        throw new NotImplementedException();
    }

    public DsfBetlogRedisDto fishGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        throw new NotImplementedException();
    }

    public DsfBetlogRedisDto fruitGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        throw new NotImplementedException();
    }
}
