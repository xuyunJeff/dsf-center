package com.invech.platform.dsfcenterdao.utlis;

import com.alibaba.druid.util.StringUtils;
import com.invech.platform.dsfcenterdao.config.ThreadLocalCache;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import com.invech.platform.dsfcenterdata.entity.DsfSite;
import com.invech.platform.dsfcenterdata.entity.SchemaThreadLocal;
import com.invech.platform.dsfcenterdata.response.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component("SiteUtil")
public class SiteUtil {

    @Autowired
    DsfSiteService dsfSiteService;

    /**
     * 异步时切换要执行的数据库,通过siteCode
     * @param siteCode
     */
    public void setDataBase(String siteCode) {
        String schema = dsfSiteService.getSchemaName(siteCode).getSchemaName();
        SchemaThreadLocal schemaThread =new SchemaThreadLocal();
        schemaThread.setSchemaName(schema);
        ThreadLocalCache.schemaThreadLocal.set(schemaThread);
    }

    public static String genRandomNum(int min,int max){
        int  maxNum = 36;
        int i;
        int count = 0;
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        int s= r.nextInt(max)%(max-min+1) + min;
        while(count < s){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**
     * 获取当前玩家对应的数据库名
     *
     * @return
     */
    public String getSchemaName() {
        RequestAttributes attr = RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attr).getRequest();
            if (request != null) {
                //注释语句从这里开始
                String siteCode = request.getHeader(ApiConstants.SITE_SECURETY_KEY);
                if (StringUtils.isEmpty(siteCode)) {
                    log.info("无法获取" + ApiConstants.SITE_SECURETY_KEY);
                    throw new RRException("无法获取" + ApiConstants.SITE_SECURETY_KEY);
                }
                return dsfSiteService.getSchemaName(siteCode).getSchemaName();
            }
        }
        return null;
    }

    /**
     * 获取当前玩家对应的站点名
     *
     * @return
     */
    public String getSiteCode() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (request != null) {
                String siteCode = request.getHeader(ApiConstants.SITE_SECURETY_KEY);
                DsfSite site =dsfSiteService.getSchemaName(siteCode);
                if (site != null && !StringUtils.isEmpty(site.getSiteName())) {
                    return siteCode;
                } else {
                    log.info("无法获取schemaName , siteCode = " + siteCode);
                }
            }
        }
        return null;
    }

    /**
     * 根据URL获取一级 domain
     * @param url
     * @return
     */
//	public static String getDomainForUrl(String url) {
//		String domainUrl = null;
//		if (url == null) {
//			return null;
//		} else if (url.contains("localhost")) {
//			return "localhost";
//		}else {
//			Pattern p = Pattern.compile("((?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv|hk|co))",Pattern.CASE_INSENSITIVE);
//			Matcher matcher = p.matcher(url);
//			while(matcher.find()){
//				domainUrl = matcher.group();
//			}
//			//增加对ip地址的支持
//			if(!matcher.find()) {
//				p = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d)))", Pattern.CASE_INSENSITIVE);
//				matcher = p.matcher(url);
//				while (matcher.find()) {
//
//					domainUrl = requestUrl(url.startsWith("http://")?url:"http://"+url);
//				}
//			}
//			return domainUrl;
//		}
//	}
}
