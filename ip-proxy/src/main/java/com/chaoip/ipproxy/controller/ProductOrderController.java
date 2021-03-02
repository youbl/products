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
import org.springframework.data.domain.Page;
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

    /**
     * 创建产品购买订单
     *
     * @param dto     订单数据
     * @param details 登录用户信息
     * @return 支付订单（不是产品订单），如果使用余额支付时，返回空
     * @throws JsonProcessingException 异常
     * @throws AlipayApiException      异常
     */
    @PostMapping("")
    public PayOrder buy(@Valid @RequestBody ProductOrderDto dto, AuthDetails details) throws Exception {
        return productOrderService.buy(dto, details.getUserName());
    }

    /**
     * 获取当前登录用户的所有产品订单
     *
     * @param details 登录用户信息
     * @return 产品订单
     */
    @GetMapping("")
    public Page<ProductOrder> getOrders(ProductOrderDto dto, AuthDetails details) {
        dto.setName(details.getUserName());
        return productOrderService.findByUser(dto);
    }

    @PostMapping("/close/{id}")
    public ProductOrder closeOrders(@PathVariable int id, AuthDetails details) {
        return productOrderService.close(id, details.getUserName());
    }
}
