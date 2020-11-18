package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.service.RouteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * ManageController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:49
 */
@RestController
@RequestMapping("manage")
public class ManageController {
    private final RouteService routeService;

    public ManageController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * 注册单个IP信息
     *
     * @param dto 单个IP
     * @return 成功数量
     */
    @PostMapping("route")
    public int addRoute(@RequestBody @Valid RouteDto dto) {
        List<RouteDto> dtos = new ArrayList<>();
        dtos.add(dto);
        return addRoute(dtos);
    }

    /**
     * 批量注册IP信息
     *
     * @param dtos 多个IP
     * @return 成功数量
     */
    @PostMapping("routes")
    public int addRoute(@RequestBody @Valid List<RouteDto> dtos) {
        return routeService.saveMultiRoute(dtos);
    }
}
