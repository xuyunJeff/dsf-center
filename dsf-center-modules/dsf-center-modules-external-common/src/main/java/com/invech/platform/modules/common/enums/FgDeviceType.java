package com.invech.platform.modules.common.enums;

import com.invech.platform.dsfcenterdata.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FgDeviceType {
    /**
     * 设备类型
     */
    PC("PC", 1, "pc"),
    IOS_H("IOS横", 2, "IOS_H"),
    IOS_S("IOS竖", 3, "IOS_S"),
    ANDROID_H("android横", 4, "ANDROID_H"),
    ANDROID_S("android竖", 5, "ANDROID_S"),
    OTHER_H("其他横", 6, "OTHER_H"),
    OTHER_S("其他竖", 7, "OTHER_S");

    private String name;
    private Integer code;
    private String type;


    private   FgDeviceType(String name, int code) {
        this.name = name;
        this.code = code;
    }


    public static String getName(int code) {
        for (FgDeviceType c : FgDeviceType.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getCode(String name) {
        for (FgDeviceType c : FgDeviceType.values()) {
            if (c.getName().equals(name)) {
                return c.code;
            }
        }
        return null;
    }

}
