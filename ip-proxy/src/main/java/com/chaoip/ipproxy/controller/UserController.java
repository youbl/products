package com.chaoip.ipproxy.controller;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.IdentifyDto;
import com.chaoip.ipproxy.controller.dto.PasswordDto;
import com.chaoip.ipproxy.controller.dto.SmsDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.ValidCode;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.ValidCodeService;
import com.chaoip.ipproxy.util.ImgHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Enumeration;
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
@Slf4j
public class UserController {
    private final BeinetUserService userService;
    private final ValidCodeService codeService;

    public UserController(BeinetUserService userService, ValidCodeService codeService) {
        this.userService = userService;
        this.codeService = codeService;
    }

    @GetMapping("")
    public BeinetUser userName(AuthDetails details) {
        if (details == null)
            return null;
        return userService.findByName(details.getUserName());
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
        if (userService.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("该手机号已被注册");
        }
        if (!codeService.validByCodeAndSn(dto.getSmsCode(), dto.getSmsSn())) {
            throw new IllegalArgumentException("短信验证码错误");
        }
        return userService.addUser(dto);
    }

    //@PreAuthorize("hasAnyRole('ROOT')")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("pwd")
    public boolean changePwd(@Valid @RequestBody PasswordDto dto, AuthDetails details) {
        if (details == null) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("两次密码输入不一致");
        }
        return userService.changePassword(dto, details.getUserName());
    }

    /**
     * 请求阿里的实名认证，并返回认证html
     * @param dto dto
     * @param details 登录信息
     * @return html
     * @throws AlipayApiException 异常
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("identify")
    public String realNameIdentify(@Valid @RequestBody IdentifyDto dto, AuthDetails details) throws AlipayApiException {
        if (!codeService.validByCodeAndSn(dto.getImgCode(), dto.getImgSn())) {
            throw new IllegalArgumentException("图形验证码错误");
        }
        if (details == null) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        return userService.realNameIdentify(dto, details.getUserName());
    }


    @GetMapping("callback")
    public String callback(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod()).append(" ").append(request.getRequestURI()).append("\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(header);
            while (values != null && values.hasMoreElements()) {
                String val = values.nextElement();
                sb.append("  ").append(header).append(" : ").append(val).append("\n");
            }
        }
        String ret = sb.toString();
        log.info(ret);
        return ret;
    }


    /**
     * 获取图形验证码和序号
     *
     * @return 序号和图片
     * @throws IOException 异常
     */
    @GetMapping("img")
    public Map<String, String> imgCode() throws IOException {
        ValidCode code = codeService.addImgCodeAndGetSn();

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
     */
    @PostMapping("sms")
    public Map<String, String> smsCode(@RequestBody SmsDto dto) {
        if (!codeService.validByCodeAndSn(dto.getCode(), dto.getSn())) {
            throw new IllegalArgumentException("图形验证码错误");
        }

        if (userService.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("该手机号已被注册");
        }

        String sn = codeService.sendSmsCode(dto);

        Map<String, String> ret = new HashMap<>();
        ret.put("sn", sn);
        return ret;
    }
}
