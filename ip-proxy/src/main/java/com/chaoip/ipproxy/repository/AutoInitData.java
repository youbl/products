package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 自动初始化数据库的类。首次启动时，新建管理员
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 22:39
 */
@Service
@Slf4j
public class AutoInitData {
    private static final String ADMIN = "sdk";
    private static final String PASSWORD = "beinet.123";
    private static final String PRODUCT_NAME = "包月代理";

    @Bean
    public CommandLineRunner commandLineRunner(BeinetUserRepository repository,
                                               PasswordEncoder encoder,
                                               ProductRepository productRepository) {
        return args -> {
            addAdmin(repository, encoder);

            addProduct(productRepository);
        };
    }

    private void addAdmin(BeinetUserRepository repository, PasswordEncoder encoder) {
        BeinetUser admin = repository.findByName(ADMIN);
        if (admin != null) {
            log.info("管理员已存在，无需创建: {}", ADMIN);
            return;
        }
        log.info("准备创建管理员: {}", ADMIN);
        admin = BeinetUser.builder()
                .name(ADMIN)
                .password(encoder.encode(PASSWORD))
                .roles("USER,ADMIN")
                .realName("超级管理员")
                .phone("13000000000")
                .build();
        admin = repository.save(admin);
        log.info("管理员创建成功: {} {}", admin.getId(), admin.getName());
    }

    private void addProduct(ProductRepository productRepository) {
        if (productRepository.findByName(PRODUCT_NAME) != null) {
            log.info("默认产品已存在，无需创建: {}", PRODUCT_NAME);
            return;
        }
        log.info("准备创建产品记录");
        Product product = Product.builder()
                .name(PRODUCT_NAME)
                .type(Product.PackageType.MONTH)
                .numPerDay(1000)
                .numPerTime(100)
                .moneyPerUnit(12000)
                .status(0)
                .build();
        productRepository.save(product);
        log.info("默认产品创建成功");
    }
}
