package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.PayOrderRepository;
import com.chaoip.ipproxy.repository.ProductOrderRepository;
import com.chaoip.ipproxy.repository.ProductRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 产品包服务类
 */
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    public ProductService(ProductRepository productRepository,
                          ProductOrderRepository productOrderRepository) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
    }

    
}
