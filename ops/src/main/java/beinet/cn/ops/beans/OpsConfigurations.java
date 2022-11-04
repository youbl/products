package beinet.cn.ops.beans;

import beinet.cn.ops.beans.ref.MyFeignClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class OpsConfigurations {
    // 默认不注入，如果yml配置里有 logging.level.beinet.cn.ops.feigns.beans.ref.MyFeignClient: true 才注入
    @Bean
    @ConditionalOnProperty("logging.level.beinet.cn.ops.feigns.beans.ref.MyFeignClient")
    MyFeignClient getClient() throws NoSuchAlgorithmException, KeyManagementException {
        // 忽略SSL校验
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return new MyFeignClient(ctx.getSocketFactory(), (hostname, sslSession) -> true);
    }

}
