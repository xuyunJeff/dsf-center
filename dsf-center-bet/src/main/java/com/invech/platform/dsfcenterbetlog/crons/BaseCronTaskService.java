package com.invech.platform.dsfcenterbetlog.crons;

import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdao.config.SpringContextUtil;
import com.invech.platform.dsfcenterdao.service.BetDaoService;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @ClassName: BaseCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 11:53:00
 * @Description: TODO
 * @Version 1.0.0
 */
@JobHandler
@Service
@Slf4j
public abstract class BaseCronTaskService extends IJobHandler {

    /**
     * 子类必须要重写这个
     */
    public GamePlatform gamePlatform;

    public PlatformType[] platformTypes;

    //第三方游戏平台的拉单task类型
    public String dsfPlatformType = null;

    @Autowired
    protected DsfSiteService dsfSiteService;

    @Autowired
    protected BetDaoService betDaoService;


    public AbstractBetlogService getBetDaoService() {
        switch (this.gamePlatform) {
            case Ds:
                return SpringContextUtil.getBean("DsBetlogService");
            case Agin:
                return SpringContextUtil.getBean("AgBetlogService");
            case Fg:
                return SpringContextUtil.getBean("FgBetlogService");
            case Ky:
                return SpringContextUtil.getBean("KyBetlogService");
            default:
                return null;
        }
    }


    /**
     * 处理调度器传过来的参数
     * <p>
     * 例: {'sTime':'2019-01-01 12:30:00','eTime':'2019-01-01 12:35:00','apiName':'ds02','redisUpdate':'true','gamePlatform':'Ds','extraKey':'0'}
     * apiName 为空时,默认全部api线路
     * gamePlatform 为空时,默认全部第三方游戏平台
     * redisUpdate 默认为 true
     *
     * @param s
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if (StringUtils.isEmpty(s)) {
            return executeBet(null, this.getApis(null));
        }
        BetTaskDto params = null;
        try {
            params = JSONObject.parseObject(s, BetTaskDto.class);
        } catch (Exception e) {
            return new ReturnT<>(500, "xxl-job-admin 参数传入异常");
        }
        //第一步获取配置,有多少个平台,有多少条api线路,拉单按照线路去拉单,拉到以后按照siteCode
        List<DsfGmApi> apiList = this.getApis(params);
        if (apiList.size() == 0) {
            return new ReturnT<>(500, "未获取任何API线路");
        }
        apiList = apiList.stream().filter(it -> it.getPlatformCode().equals(gamePlatform.platformCode)).collect(Collectors.toList());
        return executeBet(params, apiList);
    }

    /**
     * 执行拉单任务
     *
     * @param params
     * @param apiList
     * @return
     */
    public ReturnT<String> executeBet(BetTaskDto params, List<DsfGmApi> apiList) {
        if (params != null) {
            log.info(params.toString());
        }
        //apiName : DsfGmApiList
        Map<String, List<DsfGmApi>> apiMap = apiList.stream().collect(groupingBy(DsfGmApi::getApiName));
        List<String> msg = new LinkedList<>();
        apiMap.keySet().forEach(apiNames -> {
            String returnMsg = "";
            List<DsfGmApi> apis = apiMap.get(apiNames);
            DsfGmApi api = apis.get(0);
            List<String> siteCodes = apis.stream().map(it -> it.getSiteCode()).collect(Collectors.toList());
            returnMsg += "siteCodes = " + siteCodes.toString() + "api = " + api.toString() + "\n";
            //获取所有使用这条线路的站点
            BetTaskRequestParams taskRequestParams = getRedisCache(params, api);
            if (taskRequestParams == null) {
                return;
            }
            DsfBetlogRedisDto dsfBetlogRedisDto = this.executeBetGamePlatform(taskRequestParams, siteCodes, returnMsg);
            List<AbstractDsfBetlog> betlogs = dsfBetlogRedisDto.getBetlogs();
            returnMsg = dsfBetlogRedisDto.getReturnMsg();
            if (betlogs.size() == 0) {
                returnMsg += "\n 无数据获取";
                msg.add(returnMsg);
                return;
            }
            List<DsfBetLogDto<AbstractDsfBetlog>> dsfBetLogDtos = this.shareSite(betlogs.stream().filter(it -> !StringUtils.isEmpty(it.getSiteCode())).collect(Collectors.toList()));
            for (DsfBetLogDto it : dsfBetLogDtos) {
                returnMsg += "\n siteCode :" + it.getSiteCode() + " size :" + it.getT().size();
            }
            this.saveByPlatformType(dsfBetLogDtos, params, api, dsfBetlogRedisDto.getBetTaskRequestParams());
            msg.add(returnMsg);
        });
        log.info(apiList.toString());
        return new ReturnT<>(msg.toString());
    }

    public void saveByPlatformType(List<DsfBetLogDto<AbstractDsfBetlog>> dsfBetLogDtos, BetTaskDto params, DsfGmApi api, BetTaskRequestParams betTaskRequestParams) {
        if (platformTypes.length == 1) {
            dsfBetLogDtos.forEach(it -> {
                this.saveDataBase(it.getT(), it.getSiteCode(), platformTypes[0]);
                if (params.isRedisUpdate()) {
                    String key = dsfPlatformType == null ? api.getApiName() : api.getApiName() + ":" + dsfPlatformType;
                    betDaoService.putBetRedisKey(key, betTaskRequestParams);
                }
            });
        } else {
            for (PlatformType platformType : platformTypes) {
                dsfBetLogDtos.forEach(it -> {
                    List bets = it.getT().stream().filter(bet -> bet.getPlatformType() == platformType).collect(Collectors.toList());
                    this.saveDataBase(bets, it.getSiteCode(), platformType);
                    if (params.isRedisUpdate()) {
                        String key = dsfPlatformType == null ? api.getApiName() : api.getApiName() + ":" + dsfPlatformType;
                        betDaoService.putBetRedisKey(key, betTaskRequestParams);
                    }
                });
            }
        }
    }

    public abstract DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg);


    public BetTaskRequestParams getRedisCache(BetTaskDto params, DsfGmApi api) {
        BetTaskRequestParams taskRequestParams;
        BetTaskRequestParams redisParams = betDaoService.getBetRedisKey(dsfPlatformType == null ? api.getApiName() : api.getApiName() + ":" + dsfPlatformType);
        if (params == null) {
            taskRequestParams = redisParams;
        } else {
            taskRequestParams = new BetTaskRequestParams();
            if (params.getExtraKey() == null && redisParams !=null && redisParams.getBetExtraKey() != null) {
                taskRequestParams.setBetExtraKey(redisParams.getBetExtraKey());
            } else {
                taskRequestParams.setBetExtraKey(params.getExtraKey());
            }
            taskRequestParams.setDsfGmApi(api);
            if (params.getSTime() == null && redisParams !=null && redisParams.getSTime() != null) {
                taskRequestParams.setSTime(redisParams.getSTime());
            } else {
                taskRequestParams.setSTime(params.getSTime());
            }
            if (params.getETime() == null && redisParams !=null && redisParams.getETime() != null) {
                taskRequestParams.setETime(redisParams.getETime());
            } else {
                taskRequestParams.setETime(params.getETime());
            }
            if (params.isRedisUpdate()) {
                taskRequestParams.setRedisUpdate(params.isRedisUpdate());
            } else {
                taskRequestParams.setRedisUpdate(taskRequestParams.isRedisUpdate());
            }
        }
        return taskRequestParams;
    }

    /**
     * 按业主分类
     * 不处理平台用户名问题,Dao层公共类统一处理,到层要强转为子类
     *
     * @param betlogs
     * @return
     */
    public List<DsfBetLogDto<AbstractDsfBetlog>> shareSite(List<AbstractDsfBetlog> betlogs) {
        Map<String, List<AbstractDsfBetlog>> betlogsMap = betlogs.stream().collect(groupingBy(AbstractDsfBetlog::getSiteCode));
        List<DsfBetLogDto<AbstractDsfBetlog>> dsfBetLogDtos = new LinkedList<>();
        betlogsMap.keySet().forEach(it -> {
            DsfBetLogDto<AbstractDsfBetlog> dsfBetLogDto = new DsfBetLogDto<>();
            dsfBetLogDto.setSiteCode(it);
            dsfBetLogDto.setT(betlogsMap.get(it));
            dsfBetLogDtos.add(dsfBetLogDto);
        });
        return dsfBetLogDtos;
    }

    /**
     * 存到数据库
     *
     * @param betlogs
     * @param siteCode
     * @param platformType
     * @param <T>
     */
    protected <T> void saveDataBase(List<T> betlogs, String siteCode, PlatformType platformType) {
        betDaoService.insertIgnoreBetlogBatch(betlogs, siteCode, platformType);
    }

    /**
     * 根据传参apiName 获取要去采集线路
     *
     * @param params
     * @return
     */
    private List<DsfGmApi> getApis(BetTaskDto params) {
        List<DsfGmApi> apiList = dsfSiteService.queryApis();
        if (params == null) {
            return apiList;
        }
        if (params.getApiName() != null) {
            //apiName 有值判断 apiName
            String apiName = params.getApiName();
            apiList = apiList.stream().filter(it -> it.getApiName().equals(apiName)).collect(Collectors.toList());
        }
        if (params.getGamePlatform() != null) {
            apiList = apiList.stream().filter(it -> it.getPlatformCode().equals(params.getGamePlatform().platformCode)).collect(Collectors.toList());
        }
        return apiList;
    }
}
