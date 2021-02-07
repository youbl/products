package com.chaoip.ipproxy.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class WebchatPayDto {
    @JsonProperty("time_expire")
    private String timeExpire;
    @JsonProperty("amount")
    private AmountDTO amount;
    @JsonProperty("mchid")
    private String mchid;
    @JsonProperty("description")
    private String description;
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("goods_tag")
    private String goodsTag;
    @JsonProperty("appid")
    private String appid;
    @JsonProperty("attach")
    private String attach;
    @JsonProperty("detail")
    private DetailDTO detail;
    @JsonProperty("scene_info")
    private SceneInfoDTO sceneInfo;

    @NoArgsConstructor
    @Data
    public static class AmountDTO {
        /**
         * total : 100
         * currency : CNY
         */

        @JsonProperty("total")
        private Integer total;
        @JsonProperty("currency")
        private String currency;
    }

    @NoArgsConstructor
    @Data
    public static class DetailDTO {
        @JsonProperty("invoice_id")
        private String invoiceId;
        @JsonProperty("goods_detail")
        private List<GoodsDetailDTO> goodsDetail;
        @JsonProperty("cost_price")
        private Integer costPrice;

        @NoArgsConstructor
        @Data
        public static class GoodsDetailDTO {
            @JsonProperty("goods_name")
            private String goodsName;
            @JsonProperty("wechatpay_goods_id")
            private String wechatpayGoodsId;
            @JsonProperty("quantity")
            private Integer quantity;
            @JsonProperty("merchant_goods_id")
            private String merchantGoodsId;
            @JsonProperty("unit_price")
            private Integer unitPrice;
        }
    }

    @NoArgsConstructor
    @Data
    public static class SceneInfoDTO {
        @JsonProperty("store_info")
        private StoreInfoDTO storeInfo;
        @JsonProperty("device_id")
        private String deviceId;
        @JsonProperty("payer_client_ip")
        private String payerClientIp;

        @NoArgsConstructor
        @Data
        public static class StoreInfoDTO {
            @JsonProperty("address")
            private String address;
            @JsonProperty("area_code")
            private String areaCode;
            @JsonProperty("name")
            private String name;
            @JsonProperty("id")
            private String id;
        }
    }
}
