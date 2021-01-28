package beinet.cn.assetmanagement.user.repository;

import beinet.cn.assetmanagement.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
}
