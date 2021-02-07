package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.controller.dto.ProductOrderDto;
import com.chaoip.ipproxy.repository.ProductOrderDetailRepository;
import com.chaoip.ipproxy.repository.ProductOrderRepository;
import com.chaoip.ipproxy.repository.entity.*;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
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
    private final UserMoneyService userMoneyService;
    private final ProductOrderDetailRepository productOrderDetailRepository;

    public ProductOrderService(ProductService productService,
                               ProductOrderRepository productOrderRepository,
                               BeinetUserService userService,
                               PayService payService,
                               UserMoneyService userMoneyService,
                               ProductOrderDetailRepository productOrderDetailRepository) {
        this.productService = productService;
        this.productOrderRepository = productOrderRepository;
        this.userService = userService;
        this.payService = payService;
        this.userMoneyService = userMoneyService;
        this.productOrderDetailRepository = productOrderDetailRepository;
    }

    public List<ProductOrder> findAll() {
        return productOrderRepository.findByOrderByCreationTimeDesc();
    }

    public ProductOrder close(long id) {
        ProductOrder order = productOrderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("指定的订单id不存在：" + id);
        }
        order.setDisabled(1);
        return productOrderRepository.save(order);
    }

    /**
     * 根据用户名查找所有购买订单
     *
     * @param userName 用户名
     * @return 订单
     */
    public List<ProductOrder> findByUser(String userName) {
        return productOrderRepository.findByNameOrderByCreationTimeDesc(userName);
    }

    /**
     * 根据订单号查找购买订单
     *
     * @param orderNo 订单号
     * @return 订单
     */
    public ProductOrder findValidOrder(String orderNo) {
        ProductOrder order = productOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new IllegalArgumentException("订单号不存在:" + orderNo);
        }
        if (order.getStatus() != OrderStatus.SUCCESS) {
            throw new IllegalArgumentException("订单未支付成功:" + orderNo);
        }
        if (order.getDisabled() == 1) {
            throw new IllegalArgumentException("订单已关闭:" + orderNo);
        }
        // isAfter 表示 now > endTime
        if (LocalDateTime.now().isAfter(order.getEndTime())) {
            throw new IllegalArgumentException("订单已过期:" + orderNo);
        }
        order.setIpNumToday(getIpGetToday(orderNo));
        if (order.getIpNumToday() >= order.getIpNumPerDay()) {
            throw new RuntimeException("今天IP提取总数已达限制:" + order.getIpNumToday());
        }

        return order;
    }

    /**
     * 获取指定订单，今天的IP提取总数
     *
     * @param orderNo 订单号
     * @return 数量
     */
    private int getIpGetToday(String orderNo) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beign = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59, 999);
        return productOrderDetailRepository.countByOrderNoAndCreationTimeBetween(orderNo, beign, end);
    }

    /**
     * 添加ip提取记录
     *
     * @param orderNo 订单
     * @param ips     提取的ip
     */
    public void addIpGetRecord(String orderNo, List<Route> ips) {
        for (Route item : ips) {
            ProductOrderDetail detail = ProductOrderDetail.builder()
                    .orderNo(orderNo)
                    .routeId(item.getId())
                    .area(item.getArea())
                    .expireTime(item.getExpireTime())
                    .ip(item.getIp())
                    .port(item.getPort())
                    .operators(item.getOperators())
                    .protocal(item.getProtocal())
                    .build();
            productOrderDetailRepository.save(detail);
        }
    }

    /**
     * 根据支付订单id查找购买订单
     *
     * @param payOrderId 支付订单id
     * @return 订单
     */
    public ProductOrder findByPayOrderId(long payOrderId) {
        return productOrderRepository.findByPayOrderId(payOrderId);
    }

    /**
     * 设置订单成功
     *
     * @param productOrder 产品订单
     */
    public void setOrderSuccess(ProductOrder productOrder) {
        productOrder.setStatus(OrderStatus.SUCCESS);
        LocalDateTime now = LocalDateTime.now();
        productOrder.setPayTime(now); // 支付时间
        productOrder.setEndTime(now.plusMonths(productOrder.getBuyNum())); // 订单到期时间
        productOrderRepository.save(productOrder);
    }

    /**
     * 读取用户余额+产品价格，要使用事务.
     * 分布式时，要使用Redis锁.
     * 并返回支付url，余额支付时返回空
     *
     * @param dto      条件
     * @param username 用户
     * @return 支付订单，为空时表示余额支付成功
     */
    @Synchronized
    @Transactional
    public PayOrder buy(ProductOrderDto dto, String username) throws Exception {
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
                .name(username)
                .orderNo(AliBase.getTransId())
                .productId(product.getId())
                .ipNumPerDay(product.getNumPerDay())
                .buyNum(dto.getBuyNum())
                .using(dto.getUsing())
                .description(dto.getDescription())
                .money(money)
                .payType(dto.getPayType())
                .status(OrderStatus.NO_PAY)
                .build();

        PayOrder payOrder = null;
        if (dto.getPayType() == 0) {
            if (user.getMoney() < money) {
                throw new RuntimeException("用户余额不足，请先充值");
            }
            // 余额支付，进行扣款
            userMoneyService.addMoney(username, -money);
            order.setPayOrderId(0);
            setOrderSuccess(order);
        } else {
            ChargeDto chargeDto = new ChargeDto();
            chargeDto.setMoney(money);
            String desc = "购买:" + product.getName() + " 充值:" + money;
            chargeDto.setTitle(desc);
            chargeDto.setDescription(desc);
            payOrder = payService.addOrder(chargeDto, username);
            order.setPayOrderId(payOrder.getId());
            productOrderRepository.save(order);
        }

        return payOrder;
    }
}
