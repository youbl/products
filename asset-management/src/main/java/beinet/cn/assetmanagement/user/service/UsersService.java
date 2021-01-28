package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.Users;
import beinet.cn.assetmanagement.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
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

}
