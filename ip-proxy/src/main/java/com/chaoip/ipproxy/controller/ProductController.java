package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.ProductDto;
import com.chaoip.ipproxy.repository.entity.Product;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:59
 */
@RestController
@RequestMapping("product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public Page<Product> findAll(ProductDto dto) {
        return productService.findAll(dto);
    }

    @PostMapping("/status/{id}")
    public void changeStaus(@PathVariable long id) {
        productService.changeStatus(id);
    }
}
