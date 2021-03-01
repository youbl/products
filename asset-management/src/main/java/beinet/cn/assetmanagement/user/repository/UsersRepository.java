package beinet.cn.assetmanagement.user.repository;

import beinet.cn.assetmanagement.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
    List<Users> findAllByOrderByAccountAsc();

    Users findByAccount(String account);

    Users findByUserName(String userName);

    Users findByAccountOrCode(String account, String code);
}
