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

    /*
db.beinetUser.insert({ "_id" : 1, "name" : "ybl", "phone" : "12345", "password" : "52ee3687dca23050e442c90c5ca54f5b", "realName" : "", "roles" : "", "creationTime" : ISODate("2020-12-05T14:25:05.990Z"), "status" : 0, "_class" : "com.chaoip.ipproxy.repository.entity.BeinetUser" })
    * */
}
