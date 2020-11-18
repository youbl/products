package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.RouteRepository;
import org.springframework.stereotype.Service;

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

}
