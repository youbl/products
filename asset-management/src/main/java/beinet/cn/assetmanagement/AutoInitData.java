package beinet.cn.assetmanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动时自动初始化数据用
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/27 22:42
 */
@Component
@Slf4j
public class AutoInitData implements CommandLineRunner {

    // 首次启动时的，默认用户名密码
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "beinet.123";

    public AutoInitData() {

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
