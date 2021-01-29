package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.service.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("userss")
    public List<Users> findAll() {
        return usersService.findAll();
    }

    @GetMapping("users")
    public Users findById(Integer id) {
        return usersService.findById(id);
    }

    /**
     * 注册页面调用接口
     *
     * @param item
     * @return
     */
    @PostMapping("/login/users")
    public Users save(@Valid @RequestBody UsersDto item) {
        if (item == null) {
            return null;
        }
        return usersService.save(item);
    }

}
