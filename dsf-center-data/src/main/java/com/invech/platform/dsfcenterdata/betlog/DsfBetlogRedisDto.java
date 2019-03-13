package com.invech.platform.dsfcenterdata.betlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: DsfBetlogRedisDto
 * @Author: R.M.I
 * @CreateTime: 2019年03月04日 17:01:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsfBetlogRedisDto {

    private List<AbstractDsfBetlog> betlogs;

    private BetTaskRequestParams betTaskRequestParams;

    private String returnMsg;
}
