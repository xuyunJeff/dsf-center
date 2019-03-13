package com.invech.platform.dsfcenterdao.service;

import com.invech.platform.dsfcenterdao.mapper.*;
import com.invech.platform.dsfcenterdao.master.dao.SysMapper;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @ClassName: BetService
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 16:12:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@Slf4j
public class BetDaoService {

    @Autowired
    public SiteUtil siteUtil;

    @Autowired
    DsfBetlogGameVideoMapper dsfBetlogGameVideoMapper;

    @Autowired
    DsfMemberUserMapper dsfMemberUserMapper;

    @Autowired
    DsfBetlogGameVideoItemMapper dsfVideGameBetlogItemMapper;

    @Autowired
    DsfBetlogGamePokerMapper dsfBetlogGamePokerMapper;

    @Autowired
    DsfBetlogGameFishMapper dsfBetlogGameFishMapper;

    @Autowired
    DsfBetlogGameComputerMapper dsfBetlogGameComputerMapper;

    @Autowired
    SysMapper sysMapper;

    @Cacheable(value = ApiConstants.BET_API_NAME, key = "#apiName", unless="#result == null")
    public BetTaskRequestParams getBetRedisKey(String apiName) {
        return null;
    }

    @Cacheable(value = ApiConstants.BET_API_NAME, key = "#apiName+':'+#type", unless="#result == null")
    public BetTaskRequestParams getBetRedisKey(String apiName,String type) {
        return null;
    }

    @CachePut(value = ApiConstants.BET_API_NAME, key = "#apiName", unless="#result == null")
    public BetTaskRequestParams putBetRedisKey(String apiName, BetTaskRequestParams params) {
        return params;
    }

    @CachePut(value = ApiConstants.BET_API_NAME,key = "#apiName+':'+#type", unless="#result == null")
    public BetTaskRequestParams putBetRedisKey(String apiName,String type, BetTaskRequestParams params) {
        return params;
    }


    public List<DsfGmGame> gameListAg(List<String> paramCodes){
        return sysMapper.findGameListAg(paramCodes, GamePlatform.Agin.platformCode);
    }

    /**
     * 批量入库 1000条一次
     *
     * @param betlogs
     * @param siteCode
     * @param platformType
     * @param <T>
     */
    public <T> void insertIgnoreBetlogBatch(List<T> betlogs, String siteCode, PlatformType platformType) {
        int saveSize = 1000;
        int betSize = betlogs.size();
        if (saveSize >= betlogs.size()) {
            this.insertIgnoreBetlog(betlogs, siteCode, platformType);
        } else {
            int time = betSize / saveSize;
            for (int i = 0; i < time; i++) {
                this.insertIgnoreBetlog(betlogs.subList(i * saveSize, (i + 1) * saveSize - 1), siteCode, platformType);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (betSize % saveSize != 0) {
                this.insertIgnoreBetlog(betlogs.subList(time * saveSize, betSize - 1), siteCode, platformType);
            }
        }
    }

    /**
     * 根据游戏类型判断插入到那个表,忽略主键冲突
     *
     * @param betlogs
     * @param siteCode
     * @param platformType
     * @param <T>
     */
    private <T> void insertIgnoreBetlog(List<T> betlogs, String siteCode, PlatformType platformType) {
        switch (platformType) {
            case VideoGame:
                this.insertDsfBetlogGameVideoBatch(converListVideoGame(betlogs), siteCode);
                break;
            case Poker:
                this.insertDsfBetlogGamePokerBatch(converListPokerGame(betlogs), siteCode);
                break;
            case Fish:
                this.insertDsfBetlogGameFishBatch(converListFishingGame(betlogs), siteCode);
                break;
            case ComputerGame:
                this.insertComputerGameBetlogBatch(converListComputerGame(betlogs), siteCode);
                break;
        }
    }

    private void insertDsfBetlogGamePokerBatch(List<DsfBetlogGamePoker> betlogs, String siteCode) {
        CompletableFuture.runAsync(() -> {
            siteUtil.setDataBase(siteCode);
            //此处要进行给每个注单加上memberUser
            List<String> dsfPLayerIds = betlogs.stream().map(it -> it.getDsfPlayerId()).collect(Collectors.toList());
            if(dsfPLayerIds.isEmpty()){
                return;
            }
            List<DsfMemberUser> dsfMemberUsers = dsfMemberUserMapper.selectByDsfPlayerIds(dsfPLayerIds);
            List<DsfBetlogGamePoker> pokerGameBetlogs =betlogs.stream().map(it ->{
                for (DsfMemberUser user : dsfMemberUsers){
                    if(it.getDsfPlayerId().equals(user.getDsfPlayerId())){
                        it.setMemberUser(user.getMemberUser());
                    }
                }
                return it;
            }).collect(Collectors.toList());
            try {
                dsfBetlogGamePokerMapper.insertIgnoreList(pokerGameBetlogs);
            }catch (Exception e){
                log.error("数据库插入错误 ",e);
            }

        });
    }

    private void insertDsfBetlogGameFishBatch(List<DsfBetlogGameFish> betlogs, String siteCode) {
        CompletableFuture.runAsync(() -> {
            siteUtil.setDataBase(siteCode);
            //此处要进行给每个注单加上memberUser
            List<String> dsfPLayerIds = betlogs.stream().map(it -> it.getDsfPlayerId()).collect(Collectors.toList());
            if(dsfPLayerIds.isEmpty()){
                return;
            }
            List<DsfMemberUser> dsfMemberUsers = dsfMemberUserMapper.selectByDsfPlayerIds(dsfPLayerIds);
           // List<DsfBetlogGamePokerItem> itemAll = new LinkedList<>();
            List<DsfBetlogGameFish> fishingGameBetlogs =betlogs.stream().map(it ->{
                for (DsfMemberUser user : dsfMemberUsers){
                    if(it.getDsfPlayerId().equals(user.getDsfPlayerId())){
                        it.setMemberUser(user.getMemberUser());
                    }
                }
                // itemAll.addAll(it.getPokerItemList());
                return it;
            }).collect(Collectors.toList());
            try {
                dsfBetlogGameFishMapper.insertIgnoreList(fishingGameBetlogs);
            }catch (Exception e){
                log.error("数据库插入错误 ",e);
            }

        });
    }

    private void insertComputerGameBetlogBatch(List<DsfBetlogGameComputer> betlogs, String siteCode) {
        CompletableFuture.runAsync(() -> {
            siteUtil.setDataBase(siteCode);
            //此处要进行给每个注单加上memberUser
            List<String> dsfPLayerIds = betlogs.stream().map(it -> it.getDsfPlayerId()).collect(Collectors.toList());
            if(dsfPLayerIds.isEmpty()){
                return;
            }
            List<DsfMemberUser> dsfMemberUsers = dsfMemberUserMapper.selectByDsfPlayerIds(dsfPLayerIds);
            // List<DsfBetlogGamePokerItem> itemAll = new LinkedList<>();
            List<DsfBetlogGameComputer> ComputerGameBetlogs =betlogs.stream().map(it ->{
                for (DsfMemberUser user : dsfMemberUsers){
                    if(it.getDsfPlayerId().equals(user.getDsfPlayerId())){
                        it.setMemberUser(user.getMemberUser());
                    }
                }
                // itemAll.addAll(it.getPokerItemList());
                return it;
            }).collect(Collectors.toList());
            try {
                dsfBetlogGameComputerMapper.insertIgnoreList(ComputerGameBetlogs);
            }catch (Exception e){
                log.error("数据库插入错误 ",e);
            }

        });
    }



    private <T> List<DsfBetlogGameVideo> converListVideoGame(List<T> betlogs) {
        List<DsfBetlogGameVideo> dsfBetlogGameVideos = new LinkedList<>();
        for (T t : betlogs) {
            dsfBetlogGameVideos.add((DsfBetlogGameVideo) t);
        }
        return dsfBetlogGameVideos;
    }


    private <T> List<DsfBetlogGamePoker> converListPokerGame(List<T> betlogs) {
        List<DsfBetlogGamePoker> dsfBetlogGamePokers = new LinkedList<>();
        for (T t : betlogs) {
            dsfBetlogGamePokers.add((DsfBetlogGamePoker) t);
        }
        return dsfBetlogGamePokers;
    }

    private <T> List<DsfBetlogGameFish> converListFishingGame(List<T> betlogs) {
        List<DsfBetlogGameFish> dsfBetlogGameFishs = new LinkedList<>();
        for (T t : betlogs) {
            dsfBetlogGameFishs.add((DsfBetlogGameFish) t);
        }
        return dsfBetlogGameFishs;
    }

    private <T> List<DsfBetlogGameComputer> converListComputerGame(List<T> betlogs) {
        List<DsfBetlogGameComputer> DsfBetlogGameComputers = new LinkedList<>();
        for (T t : betlogs) {
            DsfBetlogGameComputers.add((DsfBetlogGameComputer) t);
        }
        return DsfBetlogGameComputers;
    }

    /**
     * 异步批量插入数据库,每次1000条;siteCode判断到那个库
     *
     * @param betlogs
     * @param siteCode
     */
    private void insertDsfBetlogGameVideoBatch(List<DsfBetlogGameVideo> betlogs, String siteCode) {
        CompletableFuture.runAsync(() -> {
            siteUtil.setDataBase(siteCode);
            //此处要进行给每个注单加上memberUser
            List<String> dsfPLayerIds = betlogs.stream().map(it -> it.getDsfPlayerId()).collect(Collectors.toList());
            if(dsfPLayerIds.isEmpty()){
                return;
            }
            List<DsfMemberUser> dsfMemberUsers = dsfMemberUserMapper.selectByDsfPlayerIds(dsfPLayerIds);
            List<DsfBetlogGameVideoItem> itemAll = new LinkedList<>();
            List<DsfBetlogGameVideo> videoGameBetlogs =betlogs.stream().map(it ->{
                for (DsfMemberUser user : dsfMemberUsers){
                    if(it.getDsfPlayerId().equals(user.getDsfPlayerId())){
                        it.setMemberUser(user.getMemberUser());
                    }
                }
                itemAll.addAll(it.getItemList());
                return it;
            }).collect(Collectors.toList());
            try {
                log.info(itemAll.toString());
                dsfVideGameBetlogItemMapper.replaceIntoList(itemAll);
                log.info(videoGameBetlogs.toString());
                dsfBetlogGameVideoMapper.replaceIntoList(videoGameBetlogs);
            }catch (Exception e){
                log.error("数据库插入错误 ",e);
            }

        });
    }
}
