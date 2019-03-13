package com.invech.platform.dsfcenterdao.config;

import com.invech.platform.dsfcenterdata.entity.SchemaThreadLocal;

/**
 * @ClassName: ThreadLocalCache
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 18:32:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class ThreadLocalCache {
    public static ThreadLocal<SchemaThreadLocal> schemaThreadLocal = new ThreadLocal<>();
}
