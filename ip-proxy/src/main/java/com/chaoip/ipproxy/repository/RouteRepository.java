package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * RouteRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface RouteRepository extends MongoRepository<Route, Long> {
    // 调用方法 routeRepository.findLikeIp("222");
    @Query(value = "{'ip':{'$regex':?0,'$options':'i'}}", fields = "{'ip':1, 'port': 1}")
    List<Route> findLikeIp(String ip);
}
