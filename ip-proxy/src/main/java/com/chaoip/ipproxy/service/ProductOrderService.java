package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ProductOrderDto;
import com.chaoip.ipproxy.repository.ProductOrderRepository;
import com.chaoip.ipproxy.repository.ProductRepository;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.Product;
import com.chaoip.ipproxy.repository.entity.ProductOrder;
import com.chaoip.ipproxy.util.AliBase;
import com.chaoip.ipproxy.util.StrHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品包服务类
 */
@Service
@Slf4j
public class ProductOrderService {
    private final ProductService productService;
    private final ProductOrderRepository productOrderRepository;

    public ProductOrderService(ProductService productService,
                               ProductOrderRepository productOrderRepository) {
        this.productService = productService;
        this.productOrderRepository = productOrderRepository;
    }

    public String buy(ProductOrderDto dto, String user) {
        Product product = productService.findById(dto.getProductId());
        if (product == null) {
            throw new RuntimeException("指定的商品id不存在：" + dto.getProductId());
        }
        int money = dto.getBuyNum() * product.getMoneyPerUnit();
        if (money != dto.getBuyMoney()) {
            throw new RuntimeException("后端价格计算与前端不一致：" + money);
        }
        ProductOrder order = ProductOrder.builder()
                .buyNum(dto.getBuyNum())
                .description(dto.getDescription())
                .using(dto.getUsing())
                .productId(dto.getProductId())
                .name(user)
                .money(money)
                .status(OrderStatus.NO_PAY)
                .orderNo(AliBase.getTransId())
                .payOrderId(xx)
                .build();
        productOrderRepository.save(order);
        return order.getOrderNo();
    }
}
