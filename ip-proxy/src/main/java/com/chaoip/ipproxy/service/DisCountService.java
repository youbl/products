package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ProductDto;
import com.chaoip.ipproxy.repository.DisCountRepository;
import com.chaoip.ipproxy.repository.ProductRepository;
import com.chaoip.ipproxy.repository.entity.DisCount;
import com.chaoip.ipproxy.repository.entity.DisCountDto;
import com.chaoip.ipproxy.repository.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品包服务类
 */
@Service
@Slf4j
public class DisCountService {
    private final DisCountRepository disCountRepository;

    public DisCountService(DisCountRepository disCountRepository) {
        this.disCountRepository = disCountRepository;
    }

    public Page<DisCount> findAll(DisCountDto dto) {// 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "offConfigs", "status", "creationTime");
        if (StringUtils.hasText(dto.getName())) {
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("name");
        }
        DisCount condition = DisCount.builder()
                .name(dto.getName())
                .build();
        Example<DisCount> example = Example.of(condition, matcher);
        return disCountRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize(), "creationTime", true);
    }

    public DisCount save(DisCountDto disCount) {
        if (disCount == null) {
            throw new RuntimeException("参数为空");
        }
        if (disCount.getOffConfigs() == null || disCount.getOffConfigs().length <= 0) {
            throw new RuntimeException("优惠配置不能为空");
        }
        DisCount old = disCountRepository.findByName(disCount.getName());
        if (old != null && old.getId() != disCount.getId()) {
            throw new RuntimeException("优惠名称已存在:" + disCount.getName());
        }
        return disCountRepository.save(disCount.mapTo());
    }

    public void changeStatus(long id) {
        DisCount disCount = disCountRepository.findById(id);
        if (disCount == null) {
            throw new RuntimeException("优惠未找到:" + id);
        }
        disCount.setStatus(disCount.getStatus() == 0 ? 1 : 0);
        disCountRepository.save(disCount);
    }

    /**
     * 根据优惠id，返回优惠明细
     *
     * @param idList 优惠id列表
     * @return 优惠明细
     */
    public List<DisCount.OffConfig> findDiscountDetail(Integer[] idList) {
        List<DisCount.OffConfig> ret = new ArrayList<>();

        List<DisCount> disCountList = disCountRepository.findAllByIdIn(idList);
        for (DisCount disCount : disCountList) {
            if (disCount.getStatus() != 0 || disCount.getOffConfigs() == null) {
                continue;
            }
            for (DisCount.OffConfig item : disCount.getOffConfigs()) {
                ret.add(item);
            }
        }

        return ret;//.stream().sorted((o1, o2) -> o2.getOff() - o1.getOff()).collect(Collectors.toList());
    }
}
