package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.IpGetService;
import com.chaoip.ipproxy.service.RouteService;
import com.chaoip.ipproxy.util.CityHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final RouteService routeService;
    private final BeinetUserService userService;
    private final IpGetService ipGetService;

    public IpController(RouteService routeService,
                        BeinetUserService userService,
                        IpGetService ipGetService) {
        this.routeService = routeService;
        this.userService = userService;
        this.ipGetService = ipGetService;
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

    /**
     * IP提取接口
     *
     * @param condition 条件
     * @param response  响应上下文
     * @return IP
     * @throws IOException 异常
     */
    @GetMapping("search")
    public List<Route> search(RouteDto condition, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(condition.getSign()) || StringUtils.isEmpty(condition.getOrderNo())) {
            throw new IllegalArgumentException("签名或订单号不能为空");
        }

        List<Route> ret = ipGetService.getIpByOrder(condition);
        if (condition.getOutputType().equalsIgnoreCase("json")) {
            initJsonData(ret);
            return ret;
        }

        outputTextIps(ret, response);
        return null;
    }

    private void initJsonData(List<Route> ret) {
        for (Route item : ret) {
            item.setId(0);
            item.setCreationTime(null);
            item.setModifyTime(null);
            item.setRndNum(null);
        }
    }

    private void outputTextIps(List<Route> ret, HttpServletResponse response) throws IOException {
        // 下面输出换行分隔的文本
        StringBuilder sb = new StringBuilder();
        for (Route item : ret) {
            sb.append(item.getIp()).append(':').append(item.getPort()).append('\n');
        }
        response.setContentType("text/plain");
        response.getOutputStream().write(sb.toString().getBytes());
        response.flushBuffer();
    }

    @GetMapping("citys")
    public Map<String, String[]> getCitys() {
        return routeService.getAllUsingCity();
    }

    @GetMapping("citys/all")
    public Map<String, String[]> getAllCitys(@RequestParam(required = false) String output,
                                             HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(output)) {
            List<String> arrCityLine = new ArrayList<>();
            for (Map.Entry<String, String[]> item : CityHelper.getCitys().entrySet()) {
                //.stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList())) {
                String[] val = item.getValue();
                String province = val[1];
                String city = val[0];
                if (!city.equals(province)) {
                    city = city.replace(province, "");
                    if (city.startsWith("省"))
                        city = city.substring(1);
                }
                String line = province + "\t" + city + "\t";
//                for (String cc : item.getValue()) {
//                    line += cc + "\t";
//                }
                arrCityLine.add(line + item.getKey());
            }
            StringBuilder sb = new StringBuilder();
            for (String item : arrCityLine.stream().sorted().collect(Collectors.toList())) {
                sb.append(item).append("\n");
            }
            response.setContentType("text/plain; charset=UTF-8");
            response.getOutputStream().write(sb.toString().getBytes());
            response.flushBuffer();
            return null;
        }
        return CityHelper.getCitys();
    }
}
