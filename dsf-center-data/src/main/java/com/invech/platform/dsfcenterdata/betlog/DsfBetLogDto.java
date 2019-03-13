package com.invech.platform.dsfcenterdata.betlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: DsfBetLogDto
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 22:27:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DsfBetLogDto<T> {
    private String siteCode;
    private List<T> t;
    private String returnMsg;

    public DsfBetLogDto(String siteCode, List<T> t) {
        this.siteCode = siteCode;
        this.t = t;
        this.returnMsg = null;
    }
}
