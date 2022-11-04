package beinet.cn.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsApplication.class, args);
    }

}
