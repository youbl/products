package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.controller.dto.ProductOrderDto;
import com.chaoip.ipproxy.repository.ProductOrderRepository;
import com.chaoip.ipproxy.repository.ProductRepository;
import com.chaoip.ipproxy.repository.entity.*;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliBase;
import com.chaoip.ipproxy.util.StrHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品包服务类
 */
@Service
@Slf4j
public class ProductOrderService {
    private final ProductService productService;
    private final ProductOrderRepository productOrderRepository;
    private final BeinetUserService userService;
    private final PayService payService;

    public ProductOrderService(ProductService productService,
                               ProductOrderRepository productOrderRepository,
                               BeinetUserService userService,
                               PayService payService) {
        this.productService = productService;
        this.productOrderRepository = productOrderRepository;
        this.userService = userService;
        this.payService = payService;
    }

    public List<ProductOrder> findAll() {
        return productOrderRepository.findByOrderByCreationTimeDesc();
    }

    public List<ProductOrder> findByUser(String userName) {
        return productOrderRepository.findByNameOrderByCreationTimeDesc(userName);
    }

    /**
     * 读取用户余额+产品价格，要使用事务.
     * 分布式时，要使用Redis锁.
     * 并返回支付url，余额支付时返回空
     *
     * @param dto      条件
     * @param username 用户
     * @return 支付url
     */
    @Transactional
    public String buy(ProductOrderDto dto, String username) throws JsonProcessingException, AlipayApiException {
        BeinetUser user = userService.findByName(username, false);
        if (user == null) {
            throw new RuntimeException("用户未找到：" + username);
        }
        Product product = productService.findById(dto.getProductId());
        if (product == null) {
            throw new RuntimeException("指定的商品id不存在：" + dto.getProductId());
        }
        int money = dto.getBuyNum() * product.getMoneyPerUnit();
        if (money != dto.getPayMoney()) {
            throw new RuntimeException("后端价格计算与前端不一致：" + money);
        }

        ProductOrder order = ProductOrder.builder()
                .buyNum(dto.getBuyNum())
                .description(dto.getDescription())
                .using(dto.getUsing())
                .productId(dto.getProductId())
                .name(username)
                .money(money)
                .status(OrderStatus.NO_PAY)
                .orderNo(AliBase.getTransId())
                .build();

        String ret = "";
        if (dto.getPayType() == 0) {
            if (user.getMoney() < money) {
                throw new RuntimeException("用户余额不足，请先充值");
            }
            // 余额支付，进行扣款
            payService.addMoney(username, -money);
            order.setPayOrderId(0);
            order.setPayTime(LocalDateTime.now());
        } else {
            ChargeDto chargeDto = new ChargeDto();
            chargeDto.setMoney(money);
            String desc = "购买:" + product.getName() + " 充值:" + money;
            chargeDto.setTitle(desc);
            chargeDto.setDescription(desc);
            PayOrder payOrder = payService.addOrder(chargeDto, username);
            order.setPayOrderId(payOrder.getId());

            ret = payOrder.getPayUrl();
        }

        productOrderRepository.save(order);
        return ret;
    }
}
