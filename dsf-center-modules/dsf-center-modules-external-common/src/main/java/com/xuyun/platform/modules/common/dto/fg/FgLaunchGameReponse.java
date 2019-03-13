package com.xuyun.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: FgLaunchGameReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 15:24:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FgLaunchGameReponse {
    private String game_url;
    private String name;
    private String token;
}
