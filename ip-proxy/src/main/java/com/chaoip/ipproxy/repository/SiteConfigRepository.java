package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.SiteConfig;

/**
 * 站点配置仓储
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface SiteConfigRepository extends BaseRepository<SiteConfig, Long> {
    SiteConfig findByKey(String key);
}
