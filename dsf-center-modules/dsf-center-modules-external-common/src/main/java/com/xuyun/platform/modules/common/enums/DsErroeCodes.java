package com.xuyun.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: DsErroeCodes
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 15:30:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DsErroeCodes {

    /*
    ○	0:　没有错误，登录成功
    ○	6001:  hashcode错误
    ○	6002:  IP未授权
    ○	6600:  Json参数格式错误
    ○	6604:  没有该游戏的权限，请联系您的上级
    ○	6606:  用户存在，但密码错误
    ○	6608:  游戏平台不支持此种币别
    ○	6609:  用户名为空, 字符长度不正确, 格式不正确
    ○	6610:  密码为空, 字符长度不正确, 格式不正确
    ○	6611:  昵称字符长度不正确
    ○	6615:  登录超时，请再试一次
    ○	6636:  账号被停用，请联系您的上级
    ○	6656:  接口访问频率，不得低于3秒，如果同一位玩家登录游戏间隔时间低于3秒的重复访问将返回此错误码
    ○	6656:  接口访问频率，不得低于10秒，如果同一个hashCode注单拉取间隔时间低于10秒的重复访问将返回此错误码
    ○	6999:  游戏维护中
    ○	6613:  无效的存款金额
    ○	6614:  无效的交易号
    ○	6616:  余额不足
    ○	6617:  交易号已存在, 操作状态为正在处理中
    ○	6625:  测试币(TEST)无法进行取款操作
    ○	6605:  无此用户
    ○	6601:  交易号已存在,操作状态为成功
     */
    Success(0,"没有错误"),HashCodeError(6001,"hashcode错误"),IPError(6002,"IP未授权"),JsonError(6600,"Json参数格式错误"),
    OutAuth(6604,"没有该游戏的权限，请联系您的上级"),
    PasswordError(6606,"用户存在，但密码错误"),CurrencyError(6608,"游戏平台不支持此种币别"),UsernameError(6609,"用户名为空, 字符长度不正确, 格式不正确"),
    PasswordError2(6610,"密码为空, 字符长度不正确, 格式不正确"),
    NicknameError(6611,"昵称字符长度不正确"),LoginError(6615,"登录超时，请再试一次"),AccountLock(6636,"账号被停用，请联系您的上级"),
    RequestError(6656,"接口访问频率，不得低于3秒，如果同一位玩家登录游戏间隔时间低于3秒的重复访问将返回此错误码"),
    GameError(6999,"游戏维护中"),InvalidMoney(6613,"无效的存款金额"),InvalidRef(6614,"无效的交易号"),LuckMoney(6616,"余额不足"),ExistRef(6617,"交易号已存在, 操作状态为正在处理中"),
    TestError(6625,"测试币(TEST)无法进行取款操作"),NoUser(6605,"无此用户"),EefSuccess(6601,"交易号已存在,操作状态为成功");

    private Integer code;

    private String msg;

    public static DsErroeCodes dsError(Integer code){
        for(DsErroeCodes dsErroeCodes : DsErroeCodes.values()){
            if(dsErroeCodes.code.equals(code)){
                return dsErroeCodes;
            }
        }
        return null;
    }
}
