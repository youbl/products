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
                .security("abcdefghijkl")
                .realName("超级管理员")
                .phone("13000000000")
                .build();
        admin = repository.save(admin);
        log.info("管理员创建成功: {} {}", admin.getId(), admin.getName());
    }

    private void addProduct(ProductRepository productRepository) {
        Product product = productRepository.findTopBy();
        if (product != null) {
            log.info("已有产品存在，无需创建: {}", product.getName());
            return;
        }
        log.info("准备创建产品记录");

        // IP有效时长数组
        Product.IpTime[] arrTime = new Product.IpTime[4];
        arrTime[0] = new Product.IpTime(3, 5, 12345);
        arrTime[1] = new Product.IpTime(5, 10, 20000);
        arrTime[2] = new Product.IpTime(10, 30, 30000);
        arrTime[3] = new Product.IpTime(30, 60, 40000);
        // 每个有效时长的价格
        Integer[] arrPrice = new Integer[]{
                100, 200, 300, 400
        };

        product = Product.builder()
                .name(PRODUCT_NAME)
                .type(Product.PackageType.MONTH)
                .numPerDay(1000)
                .numPerTime(100)
                .ipValidTime(arrTime)
                .status(0)
                .build();
        productRepository.save(product);
        log.info("默认产品创建成功");
    }
}
