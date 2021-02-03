package beinet.cn.assetmanagement.logs.repository;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatelogRepository extends JpaRepository<Operatelog, Integer> {
}
