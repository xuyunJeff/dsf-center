package com.invech.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: FgRegisterPlayerDto
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 15:21:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FgRegisterPlayerDto {
    private String member_code;
    private String password;

    @Override
    public String toString() {
        return "member_code=" + member_code + "&password=" + password ;
    }
}
