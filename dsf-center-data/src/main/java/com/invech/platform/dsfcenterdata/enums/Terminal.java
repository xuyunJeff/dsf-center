package com.invech.platform.dsfcenterdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: Terminal
 * @Author: R.M.I
 * @CreateTime: 2019年03月04日 19:39:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum Terminal {
    Mobile("手机端"),
    Computer("PC端");

    private String terminal;
}
