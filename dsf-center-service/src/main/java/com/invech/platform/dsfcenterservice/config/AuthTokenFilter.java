package com.invech.platform.dsfcenterservice.config;


import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfSite;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.dsfcenterservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class AuthTokenFilter extends HandlerInterceptorAdapter {

  @Autowired
  DsfSiteService tSiteService;

  @Autowired
  UserService userService;
  @Autowired
  SiteUtil siteUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws RRException {
    String siteCode = siteUtil.getSiteCode();
    if (siteCode == null) {
      throw new RRException("无法获取site");
    }
    DsfSite site = tSiteService.getSchemaName(siteCode);
    if (!site.getAvailable().equals(ApiConstants.SiteAvilable.AVILABLE)) {
      log.info("站点过期 site = {}" ,siteCode);
      throw new RRException("站点过期 site = " +siteCode);
    }
    String memberId = request.getHeader(ApiConstants.PLAYER_LOGGIN_KEY);
    if(StringUtils.isEmpty(memberId)){
      throw new RRException("未获取玩家信息");
    }
    return true;
  }
}
