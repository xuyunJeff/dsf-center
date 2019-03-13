package com.xuyun.platform.modules.common.dto.ag;


import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.modules.common.constants.AgConstants;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Data
//"ag数据传送
public class AgDataDto {
    //"代理编码")
    private String cagent;
    //"游戏账号长度不能大于20")
    private String loginname;
    // 方法名称简写 lg:检测并创建账号
    private String method;
    //"actype{1真钱，0试玩}")
    private Integer actype;
    //"账号密码长度不能小于20")
    private String password;
    //"盘口 默认A")
    private String oddtype;
    //"盘口 人民币 CNY")
    private String cur;
    //"回转域名")
    private String dm;
    //"序列号")
    private String sid;
    //"语言")
    private Integer lang;
    //"游戏代码")
    private String gameType;

    //"转账类型(IN:从网站账号转款到游戏账号,OUT:從遊戲账號转款到網站賬號)")
    private String type;
    //"转账金额，别名又叫额度")
    private BigDecimal credit;
    //"cagent+序列")
    private String billno;
    //"状态 值 = 1 代表调用‘预备转账成功， 值 = 0 失败 ")
    private Integer flag;
    //"y 代表 AGIN 移动网页版")
    private String mh5;


    public AgDataDto getAgDataDto(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi){
        AgDataDto agDataDto = new AgDataDto();
        agDataDto.setLoginname(dsfMemberUser.getDsfPlayerId());
        agDataDto.setPassword(dsfMemberUser.getPassword());
        agDataDto.setCur(AgConstants.CurTpye.curCny);
        agDataDto.setActype(AgConstants.Actype.trueAccount);
        agDataDto.setOddtype(AgConstants.OddTpye.OddA);
        agDataDto.setCagent(dsfGmApi.getAgyAcc());
        return agDataDto;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (!StringUtils.isEmpty(cagent)) {
            buffer.append("cagent=").append(cagent).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(loginname)) {
            buffer.append("loginname=").append(loginname).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(method)) {
            buffer.append("method=").append(method).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(actype)) {
            buffer.append("actype=").append(actype).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(password)) {
            buffer.append("password=").append(password).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(dm)) {
            buffer.append("dm=").append(dm).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(sid)) {
            buffer.append("sid=").append(cagent).append(sid).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(lang)) {
            buffer.append("lang=").append(lang).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(gameType)) {
            buffer.append("gameType=").append(gameType).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(oddtype)) {
            buffer.append("oddtype=").append(oddtype).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(cur)) {
            buffer.append("cur=").append(cur).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(type)) {
            buffer.append("type=").append(type).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(credit)) {
            buffer.append("credit=").append(credit).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(billno)) {
            buffer.append("billno=").append(billno).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(flag)) {
            buffer.append("flag=").append(flag).append("/\\\\/");
        }
        if (!StringUtils.isEmpty(mh5)) {
            buffer.append("mh5=").append(mh5).append("/\\\\/");
        }
        if (buffer.length() > 0) {
            buffer.setLength(buffer.length() - 4);
        }
        return buffer.toString();
    }

    public interface Mh5 {
        String isMobile = "y";//手机端
        String isPC = "n";//桌面端
    }
}
