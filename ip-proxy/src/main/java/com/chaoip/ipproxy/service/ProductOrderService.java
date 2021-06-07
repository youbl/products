package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.controller.dto.ProductOrderDto;
import com.chaoip.ipproxy.repository.ProductOrderDetailRepository;
import com.chaoip.ipproxy.repository.ProductOrderRepository;
import com.chaoip.ipproxy.repository.entity.*;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliBase;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final DisCountService disCountService;
    private final RouteService routeService;
    private final MongoTemplate mongoTemplate;

    public ProductOrderService(ProductService productService,
                               ProductOrderRepository productOrderRepository,
                               BeinetUserService userService,
                               PayService payService,
                               UserMoneyService userMoneyService,
                               ProductOrderDetailRepository productOrderDetailRepository,
                               DisCountService disCountService,
                               RouteService routeService,
                               MongoTemplate mongoTemplate) {
        this.productService = productService;
        this.productOrderRepository = productOrderRepository;
        this.userService = userService;
        this.payService = payService;
        this.userMoneyService = userMoneyService;
        this.productOrderDetailRepository = productOrderDetailRepository;
        this.disCountService = disCountService;
        this.routeService = routeService;
        this.mongoTemplate = mongoTemplate;
    }

    public ProductOrder close(long id, String account) {
        ProductOrder order = productOrderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("指定的订单id不存在：" + id);
        }
        if (!order.getName().equals(account)) {
            throw new RuntimeException("不能关闭他人的订单：" + id);
        }
        order.setDisabled(1);
        return productOrderRepository.save(order);
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
     * 订单延期
     *
     * @param id 订单id
     */
    public void delayOrders(long id, int days) {
        if (id <= 0 || days <= 0) {
            throw new RuntimeException("参数有误");
        }
        ProductOrder order = productOrderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("指定的订单id不存在：" + id);
        }
        order.setEndTime(order.getEndTime().plusDays(days));
        productOrderRepository.save(order);
    }

    /**
     * 根据用户名查找所有购买订单
     *
     * @param dto 条件
     * @return 订单
     */
    public Page<ProductOrder> findAll(ProductOrderDto dto) {
        //return productOrderRepository.findByNameOrderByCreationTimeDesc(userName);
        // 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "buyNum", "using", "description", "money", "payType", "payOrderId", "ipNumPerDay", "ipNumToday", "payTime", "endTime", "status", "disabled", "creationTime");
        if (StringUtils.hasText(dto.getName())) {
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("name");
        }
        if (StringUtils.hasText(dto.getOrderNo())) {
            matcher = matcher.withMatcher("orderNo", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("orderNo");
        }
        if (dto.getProductId() > 0) {
            matcher = matcher.withMatcher("productId", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("productId");
        }

        ProductOrder condition = ProductOrder.builder()
                .name(dto.getName())
                .orderNo(dto.getOrderNo())
                .productId(dto.getProductId())
                .build();
        Example<ProductOrder> example = Example.of(condition, matcher);
        return productOrderRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize(), "creationTime", true);
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

        order.setIpNumToday(getIpGetTotal(orderNo, order.getProductId()));
        if (order.getIpNumToday() >= order.getIpNumPerDay()) {
            throw new RuntimeException("当天IP提取总数已达限制:" + order.getIpNumToday());
        }

        return order;
    }

    /**
     * 获取指定订单，今天的IP提取总数，用于包月订单
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
     * 获取指定订单，总的IP提取总数
     *
     * @param orderNo 订单号
     * @return 数量
     */
    private int getIpGetTotal(String orderNo, long productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new RuntimeException("指定订单" + orderNo + "的产品" + productId + "已不存在");
        }
        if (product.getType().equals(Product.PackageType.STREAM)) {
            // 用于按量订单
            return productOrderDetailRepository.countByOrderNo(orderNo);
        }
        return getIpGetToday(orderNo);
    }

    @Transactional
    @Async
    public void updateIpGetRecord(ProductOrder order, List<Route> ips) {
        this.addIpGetRecord(order.getOrderNo(), ips);

        order.setIpNumToday(getIpGetTotal(order.getOrderNo(), order.getProductId()));
        productOrderRepository.save(order);
    }

    /**
     * 添加ip提取记录
     *
     * @param orderNo 订单
     * @param ips     提取的ip
     */
    private void addIpGetRecord(String orderNo, List<Route> ips) {
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

    public List<ProductOrderDetail> orderHistory(String orderNo) {
        return productOrderDetailRepository.findByOrderNoOrderByIdDesc(orderNo);
    }

    /**
     * 根据订单号，查找所有未过期的RouteId列表
     *
     * @param orderNo
     * @return route表的id列表
     */
    public List<Long> getRouteIdsByOrderNo(String orderNo) {
        Criteria condition = Criteria.where("orderNo").is(orderNo);

        // 过期的id就不比较了
        long minId = routeService.getMinRouteId();
        condition.and("routeId").gte(minId);
        Query query = Query.query(condition);
        return mongoTemplate.findDistinct(query, "routeId", ProductOrderDetail.class, Long.class);
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
        BeinetUser user = userService.loadUserByUsername(username);
        if (StringUtils.isEmpty(user.getRealName()) || StringUtils.isEmpty(user.getIdentity())) {
            throw new RuntimeException("用户未实名认证，不能购买：" + username);
        }

        Product product = productService.findById(dto.getProductId());
        int money = countPrice(dto, product);
        if (money != dto.getPayMoney()) {
            throw new RuntimeException("后端价格计算与前端不一致：" + money);
        }

        ProductOrder order = ProductOrder.builder()
                .name(username)
                .orderNo(AliBase.getTransId())
                .productId(product.getId())
                .ipNumPerDay(dto.getNumPerDay())
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
            // 创建一笔充值订单，并返回支付链接
            ChargeDto chargeDto = new ChargeDto();
            chargeDto.setMoney(money);
            String title = "购买:" + product.getName();
            chargeDto.setTitle(title);
            String desc = title + " 需支付:" + (money / 100) + "元";
            chargeDto.setDescription(desc);
            chargeDto.setPayType(dto.getPayType());
            // payOrder就是充值记录（也就是支付订单）
            payOrder = payService.addOrder(chargeDto, username);

            order.setPayOrderId(payOrder.getId());
            productOrderRepository.save(order);
        }

        return payOrder;
    }

    public int countPrice(ProductOrderDto dto, Product product) {
        if (product == null) {
            product = productService.findById(dto.getProductId());
        }
        if (product == null) {
            throw new RuntimeException("指定的商品id不存在：" + dto.getProductId());
        }
        if (product.getType().equals(Product.PackageType.STREAM)) {
            dto.setBuyNum(1); // 按量是按IP个数算钱，没有月数概念
        }
        if (dto.getBuyIpTime() < 0 || dto.getBuyIpTime() >= product.getIpValidTime().length) {
            throw new RuntimeException("指定的IP时长有误：" + dto.getBuyIpTime());
        }
        return countMoney(dto, product);
    }

    /**
     * 计算订单价格
     *
     * @param dto     订单信息
     * @param product 产品信息
     * @return 价格
     */
    private int countMoney(ProductOrderDto dto, Product product) {
        if (dto.getNumPerDay() < 1000 || dto.getNumPerDay() > product.getNumPerDay()) {
            throw new RuntimeException("购买IP数量必须在1000~" + product.getNumPerDay() + "之间");
        }
        if (dto.getNumPerDay() % 1000 != 0) {
            throw new RuntimeException("购买IP数量必须是1000的倍数");
        }
        int ret = dto.getBuyNum() * product.getIpValidTime()[dto.getBuyIpTime()].getPrice();

        int totalMoney = ret * (dto.getNumPerDay() / 1000);

        // 开始计算优惠
        Integer[] disCountIds = product.getDisCount();
        if (disCountIds == null || disCountIds.length == 0) {
            return totalMoney;
        }

        // 每个优惠项的计算结果金额
        HashMap<Integer, DisCount.OffConfig> disCountMoneyList = new HashMap<>();
        List<DisCount.OffConfig> configs = disCountService.findDiscountDetail(disCountIds);
        for (DisCount.OffConfig config : configs) {
            switch (config.getDisCountSource()) {
                case Month:
                    if (dto.getBuyNum() >= config.getNum()) {
                        int money = countMoneyByType(config.getType(), config.getOff(), totalMoney);
                        disCountMoneyList.put(money, config);
                    }
                    break;
                case Num:
                    if (dto.getNumPerDay() >= config.getNum()) {
                        int money = countMoneyByType(config.getType(), config.getOff(), totalMoney);
                        disCountMoneyList.put(money, config);
                    }
                    break;
                case Price:
                    if (totalMoney >= config.getNum()) {
                        int money = countMoneyByType(config.getType(), config.getOff(), totalMoney);
                        disCountMoneyList.put(money, config);
                    }
                    break;
            }
        }
        if (disCountMoneyList.isEmpty()) {
            return totalMoney;
        }
        return disCountMoneyList.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey)) // 选择金额最小的一个
                .findFirst()
                .get().getKey();
    }

    private int countMoneyByType(DisCount.DisCountType type, int offNum, int totalMoney) {
        switch (type) {
            case Percent:
                return totalMoney * offNum / 100;
            case Reduce:
                return totalMoney - offNum;
        }
        return totalMoney;
    }
}
