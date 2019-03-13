package com.xuyun.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * fg 调用不是驼峰命名
 * @ClassName: FgLaunchGameDto
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 14:59:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FgLaunchGameDto {

    //fg 调用不是驼峰命名
    private String member_code;
    private String game_code;
    private String game_type;
    private String language;
    private String ip;
    private String return_url;
    private String owner_id;

    @Override
    public String toString() {
        return "member_code=" + member_code  +
                "&game_code=" + game_code  +
                "&game_type=" + game_type  +
                "&language=" + language  +
                "&ip=" + ip  +
                "&return_url=" + return_url +
                "&owner_id=" + owner_id ;
    }
}
