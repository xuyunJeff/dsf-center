package com.invech.platform.modules.common.dto.ds.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DsDeposit
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 21:13:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DsDeposit {
    /*
       username: 合作伙伴平台的用户ID,字符串长度少于或者等于20位, 由字母和数字组成
    •　password: 合作伙伴平台的用户新密码，要求ＭＤ５加密, 长度为32位
    •　ref: 唯一交易号,由合作伙伴平台提供,以备查验,长度范围为1到32位
    •　desc: 本次交易描述,　可以为空
    •　amount: 存款金额, 格式(#.00)
     */

    private String username;
    private String password;
    private String ref;
    private String desc;
    private String amount;


}
