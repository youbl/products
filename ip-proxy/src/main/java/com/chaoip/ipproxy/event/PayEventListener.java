package com.chaoip.ipproxy.event;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.repository.entity.ProductOrder;
import com.chaoip.ipproxy.service.ProductOrderService;
import com.chaoip.ipproxy.service.UserMoneyService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 接收支付事件的处理器
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/15 20:05
 */
@Component
public class PayEventListener {
    private final UserMoneyService userMoneyService;
    private final ProductOrderService productOrderService;

    public PayEventListener(UserMoneyService userMoneyService,
                            ProductOrderService productOrderService) {
        this.userMoneyService = userMoneyService;
        this.productOrderService = productOrderService;
    }

    /**
     * 接收支付成功事件
     */
    @EventListener
    public void handlerPaySuccess(PayOrder payOrder) {
        ProductOrder productOrder = productOrderService.findByPayOrderId(payOrder.getId());
        if (productOrder == null) {
            // 如果未关联产品购买订单，则增加用户余额
            userMoneyService.addMoney(payOrder.getName(), payOrder.getMoney());
        } else {
            productOrder.setStatus(OrderStatus.SUCCESS);
            productOrderService.setOrderSuccess(productOrder);
        }
    }

}
