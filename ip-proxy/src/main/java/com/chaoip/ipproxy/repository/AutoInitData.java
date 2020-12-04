package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * AutoInitData
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

    @Bean
    public CommandLineRunner commandLineRunner(BeinetUserRepository repository, PasswordEncoder encoder) {
        return args -> {
            log.info("准备创建管理员: {}", ADMIN);
            BeinetUser admin = repository.findByName(ADMIN);
            if (admin != null) {
                log.info("管理员已存在，无需创建: {}", ADMIN);
                return;
            }
            admin = BeinetUser.builder()
                    .name(ADMIN)
                    .password(encoder.encode(PASSWORD))
                    .roles("USER,ADMIN")
                    .realName("超级管理员")
                    .phone("13000000000")
                    .creationTime(LocalDateTime.now())
                    .build();
            admin = repository.save(admin);
            log.info("管理员创建成功: {} {}", admin.getId(), admin.getName());
        };
    }

}
