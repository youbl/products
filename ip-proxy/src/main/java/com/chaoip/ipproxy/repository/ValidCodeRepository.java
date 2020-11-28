package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.ValidCode;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * RouteRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface ValidCodeRepository extends MongoRepository<ValidCode, Long> {
    /**
     * 根据序号和状态，查找第一条数据
     *
     * @param sn 序号
     * @param status 状态
     * @return 记录
     */
    ValidCode findTopBySnAndStatus(String sn, int status);
}
