package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.RouteRepository;
import com.chaoip.ipproxy.repository.entity.Route;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * RouteService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:57
 */
@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public int saveMultiRoute(List<RouteDto> dtos) {
        for (RouteDto dto : dtos) {
            routeRepository.save(dto.mapTo());
        }
        return dtos.size();
    }

    public Page<Route> getAll(RouteDto dto) {
        // 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "expireTime");
        if (!StringUtils.isEmpty(dto.getArea())) {
            matcher = matcher.withMatcher("area", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("area");
        }
        if (!StringUtils.isEmpty(dto.getOperators())) {
            matcher = matcher.withMatcher("operators", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("operators");
        }
        if (!StringUtils.isEmpty(dto.getIp())) {
            matcher = matcher.withMatcher("ip", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("ip");
        }
        if (dto.getPort() > 0) {
            matcher = matcher.withMatcher("port", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("port");
        }
        if (!StringUtils.isEmpty(dto.getProtocal())) {
            matcher = matcher.withMatcher("protocal", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("protocal");
        }
        Example<Route> example = Example.of(dto.mapTo(), matcher);
        return routeRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize());
    }
}
