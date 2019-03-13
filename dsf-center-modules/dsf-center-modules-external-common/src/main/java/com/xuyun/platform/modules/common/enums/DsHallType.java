package com.xuyun.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: DsHallType
 * @Author: R.M.I
 * @CreateTime: 2019年03月02日 14:25:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum  DsHallType {
    KKW("鼎盛娱乐城"),
    XKKW("鼎盛新厅");
    private String hallName;
}
