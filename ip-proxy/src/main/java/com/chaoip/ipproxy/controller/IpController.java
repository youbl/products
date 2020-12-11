package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.service.RouteService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * IpController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:50
 */
@RestController
@RequestMapping("ip")
public class IpController {
    private final RouteService routeService;

    public IpController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("search")
    public List<Route> search(AuthDetails details) {
        if (details == null || StringUtils.isEmpty(details.getUserName())) {
            throw new IllegalArgumentException("获取登录信息失败");
        }

        return routeService.getTop();
    }
}
