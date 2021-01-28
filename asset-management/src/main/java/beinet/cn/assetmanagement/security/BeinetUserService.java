package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.service.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * BeinetUserService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:36
 */
public class BeinetUserService implements UserDetailsService {
    private final UsersService usersService;

    /**
     * 带密码编码器的构造函数
     *
     * @param usersService 用户服务
     */
    public BeinetUserService(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * 根据用户名，查找用户信息返回。
     * 可以从数据库、内存或api远程查找
     *
     * @param username 用户名
     * @return 找到的用户信息
     * @throws UsernameNotFoundException exp
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("username can't be empty.");

        Users ret = usersService.findByAccount(username);
        if (ret == null)
            throw new UsernameNotFoundException(username + " not found.");
        return new BeinetUserDetail(ret);
    }
}
