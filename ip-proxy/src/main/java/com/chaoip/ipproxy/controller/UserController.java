package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.Result;
import com.chaoip.ipproxy.controller.dto.SmsDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.ValidCode;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.ValidCodeService;
import com.chaoip.ipproxy.util.ImgHelper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * UserController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:59
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final BeinetUserService userService;
    private final ValidCodeService codeService;

    public UserController(BeinetUserService userService, ValidCodeService codeService) {
        this.userService = userService;
        this.codeService = codeService;
    }

    @GetMapping("")
    public String userName(AuthDetails details) {
        if (details == null)
            return "未登录";
        return details.getUserName();
    }

    /**
     * 新增用户
     *
     * @param dto 新用户所需字段
     * @return 入库记录
     */
    @PostMapping("")
    public BeinetUser addUser(@Valid @RequestBody UserDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("两次密码输入不一致");
        }
        return userService.addUser(dto);
    }

    /**
     * 获取图形验证码和序号
     *
     * @return 序号和图片
     * @throws IOException 异常
     */
    @GetMapping("img")
    public Map<String, String> imgCode() throws IOException {
        ValidCode code = codeService.addCodeAndGetSn();

        Map<String, String> ret = new HashMap<>();
        ImgHelper helper = new ImgHelper();
        ret.put("img", helper.getImageBase64(code.getCode()));
        ret.put("sn", code.getSn());
        return ret;
    }

    /**
     * 获取短信验证码和序号
     *
     * @return 序号
     * @throws IOException 异常
     */
    @PostMapping("sms")
    public String smsCode(SmsDto dto) throws IOException {
        if (!codeService.validByCodeAndSn(dto.getCode(), dto.getSn())) {
            return "";
        }
        ValidCode code = codeService.addCodeAndGetSn();
        // todo sendSms;

        return code.getSn();
    }
}
