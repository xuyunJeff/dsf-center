package com.xuyun.platform.dsfcenterdata.dto;

import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: LaunchGameDto
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 11:31:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaunchGameDto {
    private String url;
    private DsfMemberUser dsfMemberUser;
}
