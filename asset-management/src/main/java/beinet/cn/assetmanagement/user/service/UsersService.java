package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.PasswordDto;
import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.model.UsersSearchDto;
import beinet.cn.assetmanagement.user.repository.UsersRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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

    public List<Users> findAll(UsersSearchDto dto) {
        List<Users> results = usersRepository.findAll(getCond(dto));//.findAllByOrderByAccountAsc();
        for (Users user : results) {
            hideSensitiveInfo(user);
        }
        return results;
    }

    private Specification getCond(UsersSearchDto dto) {
        Specification specification = (x, y, z) -> {
            ArrayList<Predicate> list = new ArrayList<>();

            if (StringUtils.hasText(dto.getCode())) {
                list.add(z.like(x.get("code"), "%" + dto.getCode() + "%"));
            }
            if (StringUtils.hasText(dto.getAccount())) {
                list.add(z.like(x.get("account"), "%" + dto.getAccount() + "%"));
            }
            if (StringUtils.hasText(dto.getUserName())) {
                list.add(z.like(x.get("userName"), "%" + dto.getUserName() + "%"));
            }
            if (dto.getState() != null && dto.getState().length > 0) {
                Predicate[] arrPredicates = new Predicate[dto.getState().length];
                for (int i = 0, j = dto.getState().length; i < j; i++) {
                    int state = dto.getState()[i];
                    arrPredicates[i] = z.equal(x.get("state"), state);
                }
                list.add(z.or(arrPredicates));
            }
            return z.and(list.toArray(new Predicate[0]));
        };
        return specification;
    }


    public Users findByAccount(String account, boolean hideSensitive) {
        Users result = usersRepository.findByAccount(account);
        if (hideSensitive) {
            hideSensitiveInfo(result);
        }
        return result;
    }

    /**
     * 根据姓名查找用户，导入用
     *
     * @param userName
     * @param hideSensitive
     * @return
     */
    public Users findByUserName(String userName, boolean hideSensitive) {
        Users result = usersRepository.findByUserName(userName);
        if (hideSensitive) {
            hideSensitiveInfo(result);
        }
        return result;
    }

    public void resetPassword(String account) {
        Users result = usersRepository.findByAccount(account);
        if (result == null)
            throw new RuntimeException("未找到用户:" + account);
        result.setPassword(getPwdEncoder().encode("123456"));
        save(result);
    }

    public Users changeUserStatus(String account) {
        Users result = usersRepository.findByAccount(account);
        if (result == null)
            throw new RuntimeException("未找到用户:" + account);
        result.setState(result.getState() == 8 ? 1 : 8);
        return save(result);
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
            // 新注册
            return insert(item);
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

    private Users insert(UsersDto item) {
        Users user = usersRepository.findByAccountOrCode(item.getAccount(), item.getCode());
        if (user != null) {
            throw new RuntimeException("此账号或工号已存在，不允许使用");
        }
        item.setPassword(getPwdEncoder().encode(item.getPassword()));
        Users newUser = item.mapTo();
        newUser.setState(0); // 新注册要审核
        newUser.setId(0);    // 确保是注册
        newUser.setRoles("USER");
        return save(newUser);
    }

    private Users update(UsersDto item, String currentAccount) {
        Users user = findByAccount(currentAccount, false);
        if (user == null) {
            throw new RuntimeException("指定的账号不存在:" + currentAccount);
        }
        user.setUserName(item.getUserName());
        user.setDepartment(item.getDepartment());
        user.setCode(item.getCode());
        if (!user.isAdmin()) {
            user.setState(0); // 修改资料要重新审核
        }
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
