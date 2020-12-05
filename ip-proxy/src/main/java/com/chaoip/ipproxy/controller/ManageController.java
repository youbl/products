package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    public ManageController(RouteService routeService, BeinetUserService userService) {
        this.routeService = routeService;
        this.userService = userService;
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
}
