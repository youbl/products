package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.repository.entity.RouteHistory;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * 路由历史操作仓储类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface RouteHistoryRepository extends BaseRepository<RouteHistory, Long> {
    // 调用方法 routeRepository.findLikeIp("222");
    @Query(value = "{'ip':{'$regex':?0,'$options':'i'}}", fields = "{'ip':1, 'port': 1}", sort = "{'creationTime':-1}")
    List<Route> findLikeIp(String ip);
}
