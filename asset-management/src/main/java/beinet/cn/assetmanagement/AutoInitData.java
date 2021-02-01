package beinet.cn.assetmanagement;

import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.service.DepartmentService;
import beinet.cn.assetmanagement.user.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动时自动初始化数据用.
 * 添加管理员，添加第一个部门
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
    private static final String FIRST_DEPART = "研发部";

    private final UsersService usersService;
    private final DepartmentService departmentService;

    public AutoInitData(UsersService usersService,
                        DepartmentService departmentService) {
        this.usersService = usersService;
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args) throws Exception {
        addAdmin();
        addFirstDepartment();
    }

    void addAdmin() {
        if (usersService.findByAccount(ADMIN, false) != null) {
            log.info("管理员已存在，无需创建: {}", ADMIN);
            return;
        }
        UsersDto admin = UsersDto.builder()
                .account(ADMIN)
                .password(PASSWORD)
                .code("0000")
                .userName("超管")
                .state(8)
                .build();
        usersService.save(admin, null);
        log.info("管理员创建成功: {} {}", admin.getId(), admin.getAccount());
    }

    void addFirstDepartment() {
        if (departmentService.findByName(FIRST_DEPART) != null) {
            log.info("部门已存在，无需创建: {}", FIRST_DEPART);
            return;
        }
        Department department = Department.builder()
                .departmentName(FIRST_DEPART)
                .build();
        departmentService.save(department);
        log.info("部门创建成功: {} {}", department.getId(), department.getDepartmentName());
    }
}
