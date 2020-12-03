package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.RouteRepository;
import com.chaoip.ipproxy.repository.entity.Route;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    public List<Route> getAll(RouteDto dto) {
        if (dto == null) {
            return routeRepository.findAll();
        }
        boolean hasCond = false;
        ExampleMatcher matcher = ExampleMatcher.matching();
        if (!StringUtils.isEmpty(dto.getArea())) {
            hasCond = true;
            matcher = matcher.withMatcher("area", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (!StringUtils.isEmpty(dto.getOperators())) {
            hasCond = true;
            matcher = matcher.withMatcher("operators", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (!StringUtils.isEmpty(dto.getIp())) {
            hasCond = true;
            matcher = matcher.withMatcher("ip", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (dto.getPort() > 0) {
            hasCond = true;
            matcher = matcher.withMatcher("port", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (!StringUtils.isEmpty(dto.getProtocal())) {
            hasCond = true;
            matcher = matcher.withMatcher("protocal", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (!hasCond) {
            return routeRepository.findAll();
        }
        Example<Route> example = Example.of(dto.mapTo(), matcher);
        return routeRepository.findAll(example);
    }
}
