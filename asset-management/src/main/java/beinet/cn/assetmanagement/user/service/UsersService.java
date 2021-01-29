package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.model.UsersDto;
import beinet.cn.assetmanagement.user.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return usersRepository.findAll();
    }

    public Users findById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }

    public Users findByAccount(String account) {
        return usersRepository.findByAccount(account);
    }

    public Users save(Users item) {
        if (item == null) {
            return null;
        }
        return usersRepository.save(item);
    }

    public Users save(UsersDto item) {
        if (item == null) {
            return null;
        }
        item.setPassword(passwordEncoder.encode(item.getPassword()));
        return save(item.mapTo());
    }
}
