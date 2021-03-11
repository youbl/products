package com.chaoip.ipproxy.controller;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.*;
import com.chaoip.ipproxy.repository.entity.*;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.service.PayService;
import com.chaoip.ipproxy.service.RealOrderService;
import com.chaoip.ipproxy.service.ValidCodeService;
import com.chaoip.ipproxy.util.ImgHelper;
import com.chaoip.ipproxy.util.QrcodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Slf4j
public class UserController {
    private final BeinetUserService userService;
    private final ValidCodeService codeService;
    private final RealOrderService realOrderService;
    private final PayService payService;

    public UserController(BeinetUserService userService,
                          ValidCodeService codeService,
                          RealOrderService realOrderService,
                          PayService payService) {
        this.userService = userService;
        this.codeService = codeService;
        this.realOrderService = realOrderService;
        this.payService = payService;
    }

    @GetMapping("")
    public BeinetUser userName(AuthDetails details) {
        if (details == null)
            return null;
        return userService.findByName(details.getUserName(), true);
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

    @PostMapping("forgetPwd")
    public String forgetPwd(@RequestBody UserDto dto) {
        if (StringUtils.isEmpty(dto.getPhone())) {
            throw new IllegalArgumentException("请提供手机号");
        }
        BeinetUser user = userService.findByPhone(dto.getPhone());
        if (user == null) {
            throw new IllegalArgumentException("该手机号不存在，请先注册");
        }
        if (!codeService.validByCodeAndSn(dto.getSmsCode(), dto.getSmsSn())) {
            throw new IllegalArgumentException("短信验证码错误");
        }
        if (StringUtils.isEmpty(dto.getPassword()) || !dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        userService.resetUserPassword(user.getId(), dto.getPassword());
        return user.getName();
    }

    /**
     * 请求阿里的实名认证，并返回认证url二维码
     *
     * @param dto     dto
     * @param details 登录信息
     * @return 二维码图片的base64字符串
     * @throws AlipayApiException 异常
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("identify")
    public String realNameIdentify(@Valid @RequestBody IdentifyDto dto, AuthDetails details) throws Exception {
        if (!codeService.validByCodeAndSn(dto.getImgCode(), dto.getImgSn())) {
            throw new IllegalArgumentException("图形验证码错误");
        }
        if (details == null) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        String url = userService.realNameIdentify(dto, details.getUserName());
        return new QrcodeHelper().getQrcode(url);
    }

    @GetMapping("qr/{orderNo}")
    public void shortUrl302(@PathVariable String orderNo, HttpServletResponse response) throws IOException {
        RealOrder code = realOrderService.findByOrder(orderNo);
        if (code == null || StringUtils.isEmpty(code.getAliUrl())) {
            throw new RuntimeException("实名认证地址不存在");
        }
        response.sendRedirect(code.getAliUrl());
    }

    /**
     * 实名认证回调接口，用于支付宝身份认证回调.
     * 注：是在手机上的支付宝访问，不是PC上.
     * 另：无论成功失败，支付宝都会回调过来，而且不带任何额外的信息。
     * 需要自己去调阿里接口进行确认
     *
     * @param orderNo 订单号
     * @return 提示语
     * @throws AlipayApiException 异常
     */
    @GetMapping("callback/{orderNo}")
    public String callback(@PathVariable(required = false) String orderNo, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(orderNo)) {
            return phoneStr("订单号不能为空", request);
        }
        RealOrder code = realOrderService.findByOrder(orderNo);
        if (code == null || StringUtils.isEmpty(code.getCertId())) {
            return phoneStr("订单不存在: " + orderNo, request);
        }

//        StringBuilder sb = new StringBuilder();
//        sb.append(request.getMethod()).append(" ").append(request.getRequestURI());
//        String strQuery = request.getQueryString();
//        if (!StringUtils.isEmpty(strQuery)) {
//            sb.append('?').append(strQuery);
//        }
//        sb.append("\n");
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames != null && headerNames.hasMoreElements()) {
//            String header = headerNames.nextElement();
//            Enumeration<String> values = request.getHeaders(header);
//            while (values != null && values.hasMoreElements()) {
//                String val = values.nextElement();
//                sb.append("  ").append(header).append(" : ").append(val).append("\n");
//            }
//        }
//        String ret = sb.toString();
//        log.info(ret);

        if (userService.realNameResultQuery(code)) {
            return phoneStr(code.getRealName() + ", 您好，您已认证成功。<p>请回到认证页面，进行刷新。</p>", request);
        }
        return phoneStr("您好，您的认证信息有误，请确认: " + code.getRealName(), request);
    }


    /**
     * 申请支付，返回支付宝支付url
     *
     * @param money 支持金额，单位分
     * @return url
     */
    @PostMapping("pay")
    public PayOrder pay(@RequestBody ChargeDto money, AuthDetails details) throws Exception {
        if (details == null) {
            throw new IllegalArgumentException("获取登录信息失败");
        }
        return payService.addOrder(money, details.getUserName());
    }

    /**
     * 支付回调接口
     *
     * @param orderNo 订单号
     * @return 提示语
     * @throws AlipayApiException 异常
     */
    @GetMapping("payback/{orderNo}")
    public String payback(@PathVariable(required = false) String orderNo, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(orderNo)) {
            return phoneStr("订单号不能为空", request);
        }
        if (payService.queryOrderStatus(orderNo)) {
            String jumpHmtlUrl = payService.getJumpHtmlUrl(orderNo);
            return jumpBack(request, jumpHmtlUrl); // 充值是在pc，可以直接jump跳回去
            // return phoneStr("您的充值已完成成功。<p>请回到充值页面，进行刷新。</p>");
        }
        return phoneStr("您的充值过程中出现问题，请稍候重试，如果已支付成功，请稍候刷新页面", request);
    }

    private String phoneStr(String msg, HttpServletRequest request) {
        if (isAjax(request))
            return msg;
        return "<html><body style='font-size:50px;'>" + msg + "</body></html>";
    }

    private String jumpBack(HttpServletRequest request, String jumpHmtlUrl) {
        if (isAjax(request))
            return "充值成功，请刷新页面";
        if (StringUtils.isEmpty(jumpHmtlUrl)) {
            jumpHmtlUrl = "/profile/user.html";
        }
        return "<html><body style='font-size:50px;'><script>location.href='" + jumpHmtlUrl + "';</script></body></html>";
    }

    private boolean isAjax(HttpServletRequest request) {
        return request.getHeader("accept").contains("application/json");
    }

    /**
     * 返回当前登录用户的充值记录
     *
     * @param dto     检索条件
     * @param details 登录人
     * @return 记录
     */
    @GetMapping("charges")
    public Page<PayOrder> getCharges(PayOrderDto dto, AuthDetails details) {
        dto.setName(details.getUserName());
        return payService.findOrder(dto);
    }

    /**
     * 返回当前登录用户的充值记录
     *
     * @param payId   记录id
     * @param details 登录人
     * @return 记录
     */
    @GetMapping("charge/{payId}")
    public PayOrder getCharges(@PathVariable int payId, AuthDetails details) {
        return payService.findById(payId, details.getUserName());
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
    public Map<String, String> smsCode(@RequestBody SmsDto dto) throws Exception {
        if (!codeService.validByCodeAndSn(dto.getCode(), dto.getSn())) {
            throw new IllegalArgumentException("图形验证码错误");
        }
        boolean isExist = userService.existsByPhone(dto.getPhone());
        if (!dto.isForgetPwd() && isExist) {
            throw new IllegalArgumentException("该手机号已被注册");
        }
        if (dto.isForgetPwd() && !isExist) {
            throw new IllegalArgumentException("该手机号还没注册");
        }

        String sn = codeService.sendSmsCode(dto);

        Map<String, String> ret = new HashMap<>();
        ret.put("sn", sn);
        return ret;
    }
}
