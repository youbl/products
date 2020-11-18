package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * RouteRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface RouteRepository extends MongoRepository<Route, Long> {
}
