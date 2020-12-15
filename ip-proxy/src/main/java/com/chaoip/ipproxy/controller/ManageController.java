package com.chaoip.ipproxy.controller;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.ManagerService;
import com.chaoip.ipproxy.service.PayService;
import com.chaoip.ipproxy.service.RouteService;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.AliPayConfig;
import com.chaoip.ipproxy.util.config.SmsConfig;
import com.chaoip.ipproxy.util.config.VerifyConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ManageController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:49
 */
@RestController
@RequestMapping("manage")
public class ManageController {
    private final RouteService routeService;
    private final BeinetUserService userService;
    private final SiteConfigService siteConfigService;
    private final ManagerService managerService;

    public ManageController(RouteService routeService,
                            BeinetUserService userService,
                            SiteConfigService siteConfigService,
                            ManagerService managerService) {
        this.routeService = routeService;
        this.userService = userService;
        this.siteConfigService = siteConfigService;
        this.managerService = managerService;
    }

    @GetMapping("routes")
    public Page<Route> getAll(RouteDto dto) {
        return routeService.getAll(dto);
    }

    /**
     * 注册单个IP信息
     *
     * @param dto 单个IP
     * @return 成功数量
     */
    @PostMapping("route")
    public int addRoute(@RequestBody @Valid RouteDto dto) {
        List<RouteDto> dtos = new ArrayList<>();
        dtos.add(dto);
        return addRoute(dtos);
    }

    /**
     * 批量注册IP信息
     *
     * @param dtos 多个IP
     * @return 成功数量
     */
    @PostMapping("routes")
    public int addRoute(@RequestBody @Valid List<RouteDto> dtos) {
        return routeService.saveMultiRoute(dtos);
    }


    /**
     * 分页获取用户列表
     *
     * @param dto 查询条件
     * @return 用户列表
     */
    @GetMapping("users")
    public Page<BeinetUser> getAllUsers(UserDto dto) {
        return userService.getAll(dto);
    }

    /**
     * 修改用户状态，启用或禁用
     *
     * @param id 用户id
     * @return 成功失败
     */
    @PostMapping("user/status/{id}")
    public boolean changeUserStatus(@PathVariable long id) {
        return userService.changeStatus(id);
    }

    /**
     * 设置或取消用户管理员状态
     *
     * @param id 用户id
     * @return 成功失败
     */
    @PostMapping("user/admin/{id}")
    public boolean changeUserAdmin(@PathVariable long id) {
        return userService.changeUserAdmin(id);
    }

    /**
     * 设置或取消用户管理员状态
     *
     * @param id 用户id
     * @return 成功失败
     */
    @PostMapping("user/password/{id}")
    public boolean resetUserPassword(@PathVariable long id) {
        return userService.resetUserPassword(id);
    }


    // 以下为读写配置的方法
    @GetMapping("config/sms")
    public SmsConfig smsConfig() throws JsonProcessingException {
        return siteConfigService.getSmsConfig();
    }

    @GetMapping("config/alipay")
    public AliPayConfig aliPayConfig() throws JsonProcessingException {
        return siteConfigService.getAliPayConfig();
    }

    @GetMapping("config/verify")
    public VerifyConfig verifyConfig() throws JsonProcessingException {
        return siteConfigService.getVerifyConfig();
    }


    @PostMapping("config/sms")
    public boolean smsConfig(@RequestBody SmsConfig config) throws JsonProcessingException {
        siteConfigService.savetSmsConfig(config);
        return true;
    }

    @PostMapping("config/alipay")
    public boolean aliPayConfig(@RequestBody AliPayConfig config) throws JsonProcessingException {
        siteConfigService.savetAliPayConfig(config);
        return true;
    }

    @PostMapping("config/verify")
    public boolean verifyConfig(@RequestBody VerifyConfig config) throws JsonProcessingException {
        siteConfigService.savetVerifyConfig(config);
        return true;
    }


    /**
     * 管理员充值
     *
     * @param money 支持金额，单位分
     * @return 成功失败
     */
    @PostMapping("pay")
    public BeinetUser pay(@RequestBody ChargeDto money, AuthDetails details) {
        if (details == null) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        money.setDescription(details.getUserName() + ":" + money.getDescription());
        managerService.chargeUser(money);
        return userService.findByName(details.getUserName(), false);
    }

    /**
     * 返回所有用户的充值记录
     *
     * @return 记录
     */
    @GetMapping("charges")
    public List<PayOrder> getCharges() {
        return managerService.findCharges();
    }
}
