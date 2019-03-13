package com.invech.platform.dsfcenterdata.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName: RedisPageHelper
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 21:19:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedisPageHelper<T> {

    private int pageSize;
    private int pageNo;
    private int totalPage;
    private List<T> page;

    public RedisPageHelper getPage(List<T> t, int pageNo, int pageSize) {
        int totalCount = t.size();
        if (totalCount == 0) {
            return new RedisPageHelper<>(0, 0,0, t);
        }
        int start = (pageNo - 1) * pageSize;
        int maxPage;
        if (totalCount % pageSize == 0) {
            maxPage = totalCount / pageSize;
        } else {
            maxPage = totalCount / pageSize + 1;
        }
        int end = pageNo * pageSize;
        if (end >= totalCount) {
            end = totalCount;
        }
        if (start > totalCount) {
            return new RedisPageHelper<>(pageSize,pageNo, maxPage, Collections.emptyList());
        }
        if (start < 0) {
            start = 0;
            end = pageNo;
        }
        return new RedisPageHelper<>(pageSize, pageNo, maxPage,t.subList(start, end));
    }
}
