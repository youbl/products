package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.DisCount;

import java.util.List;

/**
 * 优惠项表仓储
 *
 * @author youbl
 * @version 1.0
 * @date 2021/03/14 00:19
 */
public interface DisCountRepository extends BaseRepository<DisCount, Long> {

    DisCount findByName(String name);

    DisCount findById(long id);

    List<DisCount> findAllByIdIn(Integer[] idList);
}
