package com.xuyun.platform.dsfcenterservice.service;

import com.xuyun.platform.dsfcenterdao.mapper.DsfMemberUserMapper;
import com.xuyun.platform.dsfcenterdata.AbstractPlayerService;
import com.xuyun.platform.dsfcenterdata.constants.ApiConstants;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.Available;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;

import java.time.LocalDateTime;
import java.util.List;

import com.xuyun.platform.dsfcenterdata.response.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserService extends BaseService{

  @Autowired
  private DsfMemberUserMapper dsfMemberUserMapper;

  @Autowired
  private UserService userService;




  @Cacheable(value = ApiConstants.REDIS_USER_CACHE_KEY,key ="#memberUser")
  public List<DsfMemberUser> userDetails(String memberUser) {
    return dsfMemberUserMapper.select(new DsfMemberUser(memberUser));
  }


  /**
   * 检测玩家是否存在第三方账号，如果不存在就创建
   * @param memberUser
   * @param gamePlatform
   * @return
   */
  @Transactional
  public DsfMemberUser checkDsfMemberUserExists(String memberUser,GamePlatform gamePlatform){
    DsfMemberUser dsfMemberUser=dsfMemberUserMapper.selectOne(new DsfMemberUser(memberUser,gamePlatform.platformCode));
    if(dsfMemberUser == null ){
      log.info("当前玩家不存在，创建账户 memberId = {} , gamePlatform = {}",memberUser,gamePlatform);
      dsfMemberUser = registerDsfPlayer(memberUser,gamePlatform);
    }
    return dsfMemberUser;
  }

  /**
   * 创建第三方用户
   * @param memberUser
   * @param gamePlatform
   * @return
   */
  public DsfMemberUser registerDsfPlayer(String memberUser, GamePlatform gamePlatform){
    AbstractPlayerService playerService =playerService(gamePlatform);
    DsfGmApi api = super.getSiteApi(gamePlatform, siteUtil.getSiteCode());
    DsfMemberUser dsfMemberUser=new DsfMemberUser();
    dsfMemberUser.setAvailable(Available.Available.code);
    dsfMemberUser.setCreateTime(LocalDateTime.now().toString());
    dsfMemberUser.setPlatformCode(gamePlatform.platformCode);
    dsfMemberUser.setMemberUser(memberUser);
    dsfMemberUser.setUpdateTime(LocalDateTime.now().toString());
    dsfMemberUser.setSiteCode(siteUtil.getSiteCode());
    dsfMemberUser.setDsfPlayerId(playerService.generateLoginName(api));
    dsfMemberUser.setApiName(api.getApiName());
    dsfMemberUser.setPassword(playerService.generatePassword());
    //去存入数据库
    int i;
    try {
      i = userService.insertDsfMemberUser(dsfMemberUser);
    }catch (Exception e){
      log.error(e.toString());
      dsfMemberUser.setDsfPlayerId(playerService.generateLoginName(api));
      try {
        i = userService.insertDsfMemberUser(dsfMemberUser);
      }catch (Exception e2) {
        log.error(e.toString());
        throw new RRException("第三方用户名创建错误 dsfMemberUser =" + dsfMemberUser.toString());
      }
    }
    if(i == 1 ){
      //去第三方创建账户
      playerService(gamePlatform).registerPlayer(dsfMemberUser,api);
    }
    return dsfMemberUser;
  }

  @CacheEvict(value = ApiConstants.REDIS_USER_CACHE_KEY,key = "#dsfMemberUser.memberUser")
  public int insertDsfMemberUser(DsfMemberUser dsfMemberUser){
    return dsfMemberUserMapper.insert(dsfMemberUser);
  }

  public Long getMemberUser() throws RRException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes()).getRequest();
    String memberId = request.getHeader(ApiConstants.PLAYER_LOGGIN_KEY);
    return Long.valueOf(memberId);
  }
}
