package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * RouteRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface BeinetUserRepository extends BaseRepository<BeinetUser, Long> {
    BeinetUser findByName(String name);

    boolean existsByPhone(String phone);
}
