package com.chaoip.ipproxy.controller;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ProductOrderDto;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.repository.entity.ProductOrder;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.service.PayService;
import com.chaoip.ipproxy.service.ProductOrderService;
import com.chaoip.ipproxy.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 产品购买控制器类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:59
 */
@RestController
@RequestMapping("order")
@Slf4j
public class ProductOrderController {
    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final PayService payService;

    public ProductOrderController(PayService payService,
                                  ProductOrderService productOrderService,
                                  ProductService productService) {
        this.payService = payService;
        this.productOrderService = productOrderService;
        this.productService = productService;
    }

    @PostMapping("")
    public PayOrder buy(@Valid @RequestBody ProductOrderDto dto, AuthDetails details) throws JsonProcessingException, AlipayApiException {
        return productOrderService.buy(dto, details.getUserName());
    }

    @GetMapping("")
    public List<ProductOrder> getOrders(AuthDetails details) {
        return productOrderService.findByUser(details.getUserName());
    }
}
