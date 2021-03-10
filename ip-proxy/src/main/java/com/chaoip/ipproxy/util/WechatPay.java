package com.chaoip.ipproxy.util;

import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.WechatConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;

/**
 * 微信支付类
 * 文档:  https://github.com/wechatpay-apiv3/wechatpay-apache-httpclient
 *
 * @author youbl
 * @version 1.0
 * @date 2021/02/05 22:29
 */
@Component
@Slf4j
public class WechatPay {
    private static ObjectMapper mapper = new ObjectMapper();

    protected final SiteConfigService configService;

    public WechatPay(SiteConfigService configService) {
        this.configService = configService;
    }

    public WechatConfig getConfig() throws Exception {
        return configService.getWechatConfig();
    }

    /**
     * 调用支付接口.
     * 注意：金额单位为分
     *
     * @param account   支付人账号
     * @param moneyCent 支付金额，单位分
     * @return 支付宝支付url
     * @throws Exception 异常
     */
    public PayOrder getPayUrl(String account, int moneyCent, String description) throws Exception {
        WechatConfig config = getConfig();
        if (StringUtils.isEmpty(config.getPayurl()) ||
                StringUtils.isEmpty(config.getAppId()) ||
                StringUtils.isEmpty(config.getPrivateKey())) {
            throw new RuntimeException("请先进行微信支付相关配置");
        }

        HttpPost request = new HttpPost(config.getPayurl());
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        request.setHeader("Accept", "application/json");

        StringEntity se = new StringEntity(getJson(config, moneyCent, description));
        se.setContentType("text/json");
        request.setEntity(se);

        // 加载商户私钥（privateKey：私钥字符串） pem文件，或p12文件里解析出来的
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(config.getPrivateKey().getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(config.getMchId(), new PrivateKeySigner(config.getMchSerialNo(), merchantPrivateKey)), config.getApiV3Key().getBytes("utf-8"));

        // 初始化httpClient
        try (CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(config.getMchId(), config.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build()) {
            CloseableHttpResponse response = httpClient.execute(request);
            System.out.println(response);
        }
        return null;
    }

    /**
     * 查询支付状态
     *
     * @param order
     * @return
     */
    public boolean queryPayResult(PayOrder order) throws Exception {
        // https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid={mchid}
        WechatConfig config = getConfig();
        String queryUrl = config.getQueryurl()
                .replace("{transaction_id}", order.getOrderNo())
                .replace("{mchid}", config.getMchId());
        return false;
    }

    private String getJson(WechatConfig config, int moneyCent, String description) throws Exception {
        WebchatPayDto dto = new WebchatPayDto();
        dto.setAppid(config.getAppId());
        dto.setMchid(config.getMchId());

        dto.setDescription(description);
        dto.setOutTradeNo(AliBase.getTransId());
        dto.setNotifyUrl(config.getCallback());

        WebchatPayDto.AmountDTO amountDTO = new WebchatPayDto.AmountDTO();
        dto.setAmount(amountDTO);
        amountDTO.setTotal(moneyCent);

        return mapper.writeValueAsString(dto);
        /*
{
	"time_expire": "2018-06-08T10:34:56+08:00",
	"amount": {
		"total": 100,
		"currency": "CNY"
	},
	"mchid": "1230000109",
	"description": "Image形象店-深圳腾大-QQ公仔",
	"notify_url": "https://www.weixin.qq.com/wxpay/pay.php",
	"out_trade_no": "1217752501201407033233368018",
	"goods_tag": "WXG",
	"appid": "wxd678efh567hg6787",
	"attach": "自定义数据说明",
	"detail": {
		"invoice_id": "wx123",
		"goods_detail": [{
			"goods_name": "iPhoneX 256G",
			"wechatpay_goods_id": "1001",
			"quantity": 1,
			"merchant_goods_id": "商品编码",
			"unit_price": 828800
		}, {
			"goods_name": "iPhoneX 256G",
			"wechatpay_goods_id": "1001",
			"quantity": 1,
			"merchant_goods_id": "商品编码",
			"unit_price": 828800
		}],
		"cost_price": 608800
	},
	"scene_info": {
		"store_info": {
			"address": "广东省深圳市南山区科技中一道10000号",
			"area_code": "440305",
			"name": "腾讯大厦分店",
			"id": "0001"
		},
		"device_id": "013467007045764",
		"payer_client_ip": "14.23.150.211"
	}
}
        * */
    }

    /**
     * 把分的单位转换为元，并保留2位小数
     *
     * @param moneyCent 金额，单位分
     * @return 单位:元的字符串
     */
    private static String transMoneyToYuan(int moneyCent) {
        return String.format("%.2f", moneyCent / 100F);
    }

    /**
     * 把元的单位转换为分，并取整
     *
     * @param moneyStr 金额，单位元
     * @return 单位:分的整数金额
     */
    private static int transMoneyToCent(String moneyStr) {
        float money = Float.parseFloat(moneyStr) * 100;
        return (int) money;
    }
}
