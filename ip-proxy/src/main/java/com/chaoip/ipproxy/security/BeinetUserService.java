package com.chaoip.ipproxy.security;

import com.chaoip.ipproxy.controller.dto.IdentifyDto;
import com.chaoip.ipproxy.controller.dto.PasswordDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.BeinetUserRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * BeinetUserService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:36
 */
public class BeinetUserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final BeinetUserRepository userRepository;
    private static final String noUserMsg = "指定的用户未找到: ";


    /**
     * 带密码编码器的构造函数
     *
     * @param encoder 编码器
     */
    public BeinetUserService(PasswordEncoder encoder, BeinetUserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
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

        BeinetUser ret = userRepository.findByName(username);
        if (ret == null)
            throw new UsernameNotFoundException(username + " not found.");
        return ret;
    }

    /**
     * 根据手机查找用户是否存在
     *
     * @param phone 手机号
     * @return 是否
     */
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 添加用户
     *
     * @param userDto dto
     * @return 添加后的对象
     */
    public BeinetUser addUser(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()) != null) {
            throw new RuntimeException("该名称已存在:" + userDto.getName());
        }
        BeinetUser user = userDto.mapTo();
        // 使用登录的密码加密器进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 根据登录账号查找用户
     *
     * @param name 账号
     * @return 对象
     */
    public BeinetUser findByName(String name) {
        BeinetUser user = userRepository.findByName(name);

        if (user != null) {
            String replace = "$1****$2";
            // 隐藏密码
            user.setPassword("");
            // 隐藏手机号
            if (!StringUtils.isEmpty(user.getPhone())) {
                user.setPhone(user.getPhone().replaceAll("^(.{3}).+(.{2})$", replace));
            }
            // 隐藏银行卡
            if (!StringUtils.isEmpty(user.getBankNo())) {
                user.setBankNo(user.getBankNo().replaceAll("^(.{3}).+(.{3})$", replace));
            }
            // 隐藏SecurityKey
            if (!StringUtils.isEmpty(user.getSecurity())) {
                user.setSecurity(user.getSecurity().replaceAll("^(.{4}).+(.{4})$", replace));
            }
            // 隐藏身份证
            if (!StringUtils.isEmpty(user.getIdentity())) {
                user.setIdentity(user.getIdentity().replaceAll("^(.{4}).+(.{4})$", replace));
            }
        }
        return user;
    }

    /**
     * 根据账号和旧密码，修改密码
     *
     * @param dto      dto
     * @param username 账号
     * @return 修改成功失败
     */
    public boolean changePassword(PasswordDto dto, String username) {
        BeinetUser user = userRepository.findByName(username);
        if (user == null) {
            throw new RuntimeException(noUserMsg + username);
        }
        if (!encoder.matches(dto.getPasswordOld(), user.getPassword())) {
            throw new RuntimeException("输入的旧密码不匹配: " + username);
        }
        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * 实名认证，通过后保存用户名
     *
     * @param dto      dto
     * @param username 账号
     * @return 是否
     */
    public boolean realNameIdentify(IdentifyDto dto, String username) {
        BeinetUser user = userRepository.findByName(username);
        if (user == null) {
            throw new RuntimeException(noUserMsg + username);
        }


        user.setRealName(dto.getRealName());
        user.setIdentity(dto.getIdentity());
        userRepository.save(user);
        return true;
    }

    /**
     * 分页获取用户列表
     *
     * @param dto 条件
     * @return 用户列表
     */
    public Page<BeinetUser> getAll(UserDto dto) {
        // 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "password", "security", "status");

        BeinetUser condition = dto.mapTo();
        if (!StringUtils.isEmpty(dto.getName())) {
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matcher = matcher.withIgnorePaths("name");
        }
        if (!StringUtils.isEmpty(dto.getPhone())) {
            matcher = matcher.withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matcher = matcher.withIgnorePaths("phone");
        }
        if (!StringUtils.isEmpty(dto.getBankNo())) {
            condition.setBankNo(dto.getBankNo());
            matcher = matcher.withMatcher("bankNo", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matcher = matcher.withIgnorePaths("bankNo");
        }
        if (!StringUtils.isEmpty(dto.getIdentity())) {
            condition.setIdentity(dto.getIdentity());
            matcher = matcher.withMatcher("identity", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matcher = matcher.withIgnorePaths("identity");
        }
        if (!StringUtils.isEmpty(dto.getRealName())) {
            condition.setRealName(dto.getRealName());
            matcher = matcher.withMatcher("realName", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matcher = matcher.withIgnorePaths("realName");
        }
        // 不支持时间范围，参考 https://stackoverflow.com/questions/45973070/spring-jpa-examplematcher-compare-date-condition
//        if (dto.getCreationTime() != null) {
//            condition.setRealName(dto.getRealName());
//            matcher = matcher.withMatcher("creationTime", ExampleMatcher.GenericPropertyMatchers.contains());
//        } else {
        matcher = matcher.withIgnorePaths("creationTime");
//        }
        Example<BeinetUser> example = Example.of(condition, matcher);
        return userRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize());
    }


    /**
     * 修改指定用户的状态，禁用或启用
     *
     * @param id 用户id
     * @return 成败
     */
    public boolean changeStatus(long id) {
        BeinetUser user = findById(id);
        user.setStatus(user.getStatus() == 0 ? 1 : 0);
        userRepository.save(user);
        return true;
    }

    /**
     * 设置或取消用户管理员状态
     *
     * @param id 用户id
     * @return 成功失败
     */
    public boolean changeUserAdmin(long id) {
        BeinetUser user = findById(id);
        String admin = "ADMIN";
        String roles = user.getRoles();
        if (roles == null) {
            roles = "";
        }
        if (roles.contains("ADMIN")) {
            roles = roles.replaceAll(admin + ",?", "");
        } else {
            roles += (roles.endsWith(",") ? "" : ",") + admin;
        }
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    /**
     * 设置或取消用户管理员状态
     *
     * @param id 用户id
     * @return 成功失败
     */
    public boolean resetUserPassword(long id) {
        BeinetUser user = findById(id);
        user.setPassword(encoder.encode("123456"));
        userRepository.save(user);
        return true;
    }

    private BeinetUser findById(long id) {
        Optional<BeinetUser> opnUser = userRepository.findById(id);
        if (!opnUser.isPresent()) {
            throw new RuntimeException(noUserMsg + id);
        }
        return opnUser.get();
    }

}
