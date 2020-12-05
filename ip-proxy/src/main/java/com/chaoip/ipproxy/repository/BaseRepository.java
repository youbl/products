package com.chaoip.ipproxy.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BaseRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/5 9:32
 */
@NoRepositoryBean // 这个类不创建bean
public interface BaseRepository<T, ID> extends MongoRepository<T, ID> {
    /**
     * 分页查找，并返回数据
     *
     * @param example  查找条件
     * @param pageNum  第几页，从0开始
     * @param pageSize 每页几条
     * @return 分页对象
     */
    default Page<T> pageSearch(Example<T> example, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 0);
        pageSize = pageSize > 0 && pageSize < 100 ? pageSize : 20;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        // getContent是记录，getTotalElements是总记录数，用于前端分页
        return findAll(example, pageable);
    }
}
