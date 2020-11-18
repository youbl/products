package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
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
    @GetMapping("search")
    public List<RouteDto> search(RouteDto condition) {
        return new ArrayList<>();
    }
}
