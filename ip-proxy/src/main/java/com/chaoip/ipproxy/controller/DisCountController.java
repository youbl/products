package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.ProductDto;
import com.chaoip.ipproxy.repository.entity.DisCount;
import com.chaoip.ipproxy.repository.entity.DisCountDto;
import com.chaoip.ipproxy.repository.entity.Product;
import com.chaoip.ipproxy.service.DisCountService;
import com.chaoip.ipproxy.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * ProductController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:59
 */
@RestController
@RequestMapping("disCount")
@Slf4j
public class DisCountController {
    private final DisCountService disCountService;

    public DisCountController(DisCountService disCountService) {
        this.disCountService = disCountService;
    }

    @GetMapping("/all")
    public Page<DisCount> findAll(DisCountDto dto) {
        return disCountService.findAll(dto);
    }

    @PostMapping("")
    public void saveData(@RequestBody DisCountDto dto) {
        disCountService.save(dto);
    }
}
