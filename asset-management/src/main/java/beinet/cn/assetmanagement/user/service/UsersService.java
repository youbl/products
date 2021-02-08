package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.PasswordDto;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.repository.UsersRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    // ObjectProvider 用于避免循环依赖
    private final ObjectProvider<PasswordEncoder> passwordEncoderProvider;
    private PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        ObjectProvider<PasswordEncoder> passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoderProvider = passwordEncoder;
    }

    private PasswordEncoder getPwdEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = passwordEncoderProvider.getIfAvailable();
            assert passwordEncoder != null;
        }
        return passwordEncoder;
    }

    public List<Users> findAll() {
        List<Users> results = usersRepository.findAllByOrderByAccountAsc();
        for (Users user : results) {
            hideSensitiveInfo(user);
        }
        return results;
    }

    public Users findByAccount(String account, boolean hideSensitive) {
        Users result = usersRepository.findByAccount(account);
        if (hideSensitive) {
            hideSensitiveInfo(result);
        }
        return result;
    }

    public void resetPassword(int id) {
        Users result = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("指定id未找到用户:" + id));
        result.setPassword(getPwdEncoder().encode("123456"));
        save(result);
    }

    public void changeUserStatus(int id) {
        Users result = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("指定id未找到用户:" + id));
        result.setState(result.getState() == 8 ? 1 : 8);
        save(result);
    }

    public Users save(Users item) {
        if (item == null) {
            return null;
        }
        if (isSensitiveInfoHided(item)) {
            throw new RuntimeException("敏感信息被隐藏过的数据，不能用于保存");
        }
        return usersRepository.save(item);
    }

    public void changePassword(PasswordDto dto, String currentAccount) {
        Users user = findByAccount(currentAccount, false);
        if (user == null) {
            throw new RuntimeException("指定的账号不存在:" + currentAccount);
        }
        if (!getPwdEncoder().matches(dto.getPasswordOld(), user.getPassword())) {
            throw new RuntimeException("输入的旧密码不匹配: " + currentAccount);
        }
        user.setPassword(getPwdEncoder().encode(dto.getPassword()));
        save(user);
    }

    public Users saveByUser(UsersDto item, String currentAccount) {
        if (item == null) {
            return null;
        }
        if (StringUtils.isEmpty(currentAccount) || currentAccount.equals("匿名")) {
            // 注册
            item.setPassword(getPwdEncoder().encode(item.getPassword()));
            Users newUser = item.mapTo();
            newUser.setRoles("USER");
            return save(newUser);
        }
        // 已登录时，表示更新资料
        return update(item, currentAccount);
    }

    public Users saveByAdmin(UsersDto item) {
        Users user;
        if (item.getId() > 0) {
            user = usersRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("指定的id找不到用户:" + item.getId()));
            if (!user.getAccount().equals(item.getAccount())) {
                throw new RuntimeException("不允许修改登录账号:" + user.getAccount());
            }
            user.setCode(item.getCode());
            user.setUserName(item.getUserName());
            user.setDepartment(item.getDepartment());
            user.setRoles(item.getRoles());
            user.setState(item.getState());
        } else {
            user = item.mapTo();
            user.setPassword(getPwdEncoder().encode(user.getPassword()));
        }
        return save(user);
    }

    private Users update(UsersDto item, String currentAccount) {
        Users user = findByAccount(currentAccount, false);
        if (user == null) {
            throw new RuntimeException("指定的账号不存在:" + currentAccount);
        }
        user.setUserName(item.getUserName());
        user.setDepartment(item.getDepartment());
        user.setCode(item.getCode());
        return save(user);
    }

    private void hideSensitiveInfo(Users user) {
        user.setPassword("");

        String replace = "$1****$2";
        // 隐藏手机号
        if (!StringUtils.isEmpty(user.getPhone())) {
            user.setPhone(user.getPhone().replaceAll("^(.{3}).+(.{2})$", replace));
        }
    }

    private boolean isSensitiveInfoHided(Users user) {
        return StringUtils.isEmpty(user.getPassword());
    }
}
