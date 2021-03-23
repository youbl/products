package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.ProductOrder;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.ProductOrderService;
import com.chaoip.ipproxy.service.RouteService;
import com.chaoip.ipproxy.util.SecurityHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * IpController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:50
 */
@RestController
@RequestMapping("ip")
public class IpController {
    private final ProductOrderService productOrderService;
    private final RouteService routeService;
    private final BeinetUserService userService;

    public IpController(RouteService routeService,
                        BeinetUserService userService,
                        ProductOrderService productOrderService) {
        this.routeService = routeService;
        this.userService = userService;
        this.productOrderService = productOrderService;
    }

    @GetMapping("url")
    public String getUrl(RouteDto condition, AuthDetails details, HttpServletRequest request) {
        if (details == null || StringUtils.isEmpty(details.getUserName())) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        BeinetUser user = userService.loadUserByUsername(details.getUserName());
        if (StringUtils.isEmpty(user.getSecurity())) {
            throw new IllegalArgumentException("请先去个人信息重置SecurityKey");
        }
        return "/ip/search?sign=" + user.getSecurity() + '&' + request.getQueryString();
    }

    @GetMapping("search")
    public List<Route> search(RouteDto condition, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(condition.getSign()) || StringUtils.isEmpty(condition.getOrderNo())) {
            throw new IllegalArgumentException("签名或订单号不能为空");
        }
        ProductOrder order = productOrderService.findValidOrder(condition.getOrderNo());
        BeinetUser user = userService.loadUserByUsername(order.getName());
        if (!user.getSecurity().equals(condition.getSign())) {
            throw new IllegalArgumentException("签名不匹配:" + condition.getSign());
        }

        int leftNum = (order.getIpNumPerDay() - order.getIpNumToday());// 剩余可提取个数
        int limitNum = Math.min(leftNum, condition.getPageSize());
        if (limitNum <= 0) {
            limitNum = 10;
        }
        condition.setPageSize(limitNum);

        // 查路由数据
        List<Route> ret = routeService.find(condition);
        if (ret == null || ret.isEmpty()) {
            return null;
        }

        productOrderService.addIpGetRecord(order.getOrderNo(), ret);

        if (condition.getOutputType().equalsIgnoreCase("json")) {
            return ret;
        }
        // 下面输出换行分隔的文本
        StringBuilder sb = new StringBuilder();
        for (Route item : ret) {
            sb.append(item.getIp()).append(':').append(item.getPort()).append('\n');
        }
        response.setContentType("text/plain");
        response.getOutputStream().write(sb.toString().getBytes());
        response.flushBuffer();
        return null;
    }

    @GetMapping("citys")
    public Map<String, String[]> getCitys() {
        return routeService.getAllUsingCity();
    }
}
