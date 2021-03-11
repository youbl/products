package com.chaoip.ipproxy.security;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.IdentifyDto;
import com.chaoip.ipproxy.controller.dto.PasswordDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.BeinetUserRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.RealOrder;
import com.chaoip.ipproxy.service.RealOrderService;
import com.chaoip.ipproxy.util.VerifyHelper;
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
    private final RealOrderService realOrderService;
    private final VerifyHelper verifyHelper;

    private static final String noUserMsg = "指定的用户未找到: ";


    /**
     * 带密码编码器的构造函数
     *
     * @param encoder 编码器
     */
    public BeinetUserService(PasswordEncoder encoder, BeinetUserRepository userRepository, RealOrderService realOrderService, VerifyHelper verifyHelper) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.realOrderService = realOrderService;
        this.verifyHelper = verifyHelper;
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
     * 根据手机查找用户
     *
     * @param phone 手机号
     * @return 用户
     */
    public BeinetUser findByPhone(String phone) {
        return userRepository.findByPhone(phone);
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
        return save(user);
    }

    /**
     * 根据登录账号查找用户
     *
     * @param name 账号
     * @param hide 是否隐藏重要属性
     * @return 对象
     */
    public BeinetUser findByName(String name, boolean hide) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        BeinetUser user = userRepository.findByName(name);

        if (hide && user != null) {
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
        save(user);
        return true;
    }

    /**
     * 实名认证，通过后保存用户名。
     * 注：要使用短码，因为阿里返回的url太长，生成的二维码可能无法扫描
     *
     * @param dto      dto
     * @param username 账号
     * @return 实名认证的短码地址
     */
    public String realNameIdentify(IdentifyDto dto, String username) throws Exception {
        BeinetUser user = userRepository.findByName(username);
        if (user == null) {
            throw new RuntimeException(noUserMsg + username);
        }
        RealOrder code = verifyHelper.getVerifyData(username, dto.getRealName(), dto.getIdentity());
        realOrderService.addOrder(code);

        return verifyHelper.getShortUrl(code.getOrderNo());
    }

    /**
     * 查询实名认证结果，通过时，要设置为已实名认证
     *
     * @param code 认证信息
     * @return 是否通过
     * @throws AlipayApiException 异常
     */
    public boolean realNameResultQuery(RealOrder code) throws Exception {
        boolean ret = verifyHelper.queryValidate(code.getCertId(), code.getName());
        if (ret) {
            BeinetUser user = userRepository.findByName(code.getName());
            if (user == null) {
                throw new RuntimeException("指定的账号不存在，实名认证有误？？" + code.getName());
            }
            user.setRealName(code.getRealName());
            user.setIdentity(code.getIdentity());
            save(user);
        }
        return ret;
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
        return userRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize(), "creationTime", true);
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
        save(user);
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
        save(user);
        return true;
    }

    /**
     * 设置或取消用户管理员状态
     *
     * @param id     用户id
     * @param newpwd 用户新密码
     * @return 成功失败
     */
    public boolean resetUserPassword(long id, String newpwd) {
        BeinetUser user = findById(id);
        user.setPassword(encoder.encode(newpwd));
        save(user);
        return true;
    }

    private BeinetUser findById(long id) {
        Optional<BeinetUser> opnUser = userRepository.findById(id);
        if (!opnUser.isPresent()) {
            throw new RuntimeException(noUserMsg + id);
        }
        return opnUser.get();
    }

    public BeinetUser save(BeinetUser user) {
        if (user.getPhone().contains("****")) {
            throw new RuntimeException("手机号变成星号了……" + user.getName());
        }

        return userRepository.save(user);
    }
}
