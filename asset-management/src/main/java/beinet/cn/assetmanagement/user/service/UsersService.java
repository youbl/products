package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.PasswordDto;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> findAll() {
        List<Users> results = usersRepository.findAll();
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
        if (!passwordEncoder.matches(dto.getPasswordOld(), user.getPassword())) {
            throw new RuntimeException("输入的旧密码不匹配: " + currentAccount);
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        save(user);
    }

    public Users save(UsersDto item, String currentAccount) {
        if (item == null) {
            return null;
        }
        if (StringUtils.isEmpty(currentAccount) || currentAccount.equals("匿名")) {
            item.setPassword(passwordEncoder.encode(item.getPassword()));
            return save(item.mapTo());
        }
        // 已登录时，表示更新资料
        return update(item, currentAccount);
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
