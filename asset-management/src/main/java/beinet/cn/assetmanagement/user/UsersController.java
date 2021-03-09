package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.event.EventDto;
import beinet.cn.assetmanagement.event.Publisher;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.security.AuthDetails;
import beinet.cn.assetmanagement.user.model.PasswordDto;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.model.UsersSearchDto;
import beinet.cn.assetmanagement.user.service.UsersService;
import beinet.cn.assetmanagement.utils.RequestHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/login/currentUser")
    public Users getCurrentUser(AuthDetails details) {
        if (details.getAccount().equals("匿名")) {
            return null;
        }
        return usersService.findByAccount(details.getAccount(), true);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")// 分类管理员的资产审核需要名字
    @GetMapping("/users")
    public List<Users> findAll(UsersSearchDto dto) {
        return usersService.findAll(dto);
    }

    /**
     * 管理员编辑用户接口
     *
     * @param dto     用户
     * @param request 上下文
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/users")
    public void editUser(@Valid @RequestBody UsersDto dto,
                         HttpServletRequest request,
                         AuthDetails details) {
        if (dto.getId() <= 0) {
            dto.setPassword("123456"); // 管理员新增时，要设置默认密码
        }
        usersService.saveByAdmin(dto);

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type((dto.getId() > 0) ? OperateEnum.AdminModify : OperateEnum.AdminAdd)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(dto)
                .build());
    }

    /**
     * 禁用 启用接口
     *
     * @param account 用户账号
     * @param details 登录信息
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/users/state/{account}")
    public void changeUserStatus(@PathVariable String account,
                                 AuthDetails details,
                                 HttpServletRequest request) {
        Users user = usersService.changeUserStatus(account);

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type(OperateEnum.AdminChgState)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(user)
                .build());
    }

    /**
     * 重置密码接口
     *
     * @param account 用户账号
     * @param details 登录信息
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/users/password/{account}")
    public void resetPassword(@PathVariable String account,
                              AuthDetails details,
                              HttpServletRequest request) {
        usersService.resetPassword(account);

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type(OperateEnum.AdminResetPwd)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(account)
                .build());
    }

    /**
     * 修改密码接口
     *
     * @param item    数据dto
     * @param details 当前登录信息
     */
    @PostMapping("/users/password")
    public void changePassword(@Valid @RequestBody PasswordDto item,
                               AuthDetails details,
                               HttpServletRequest request) {
        if (item == null) {
            return;
        }
        if (!details.getAccount().equals(item.getAccount())) {
            throw new RuntimeException("登录账号不一致");
        }
        usersService.changePassword(item, details.getAccount());

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type(OperateEnum.ChangePwd)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(item)
                .build());
    }

    /**
     * 注册页面或个人资料修改 调用接口
     *
     * @param item    前端提交信息
     * @param details 登录信息，匿名表示注册，否则表示修改个人资料
     * @return 修改后的信息
     */
    @PostMapping("/login/users")
    public Users save(@Valid @RequestBody UsersDto item,
                      AuthDetails details,
                      HttpServletRequest request) {
        if (item == null) {
            return null;
        }
        if (item.getAccount().equals("匿名")) {
            throw new RuntimeException("不允许账号使用 匿名");
        }
        if (!details.getAccount().equals("匿名") &&
                !details.getAccount().equals(item.getAccount())) {
            throw new RuntimeException("登录账号不允许修改");
        }
        Users ret = usersService.saveByUser(item, details.getAccount());

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type((item.getId() > 0) ? OperateEnum.Modify : OperateEnum.Register)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(item)
                .build());

        return ret;
    }

}
