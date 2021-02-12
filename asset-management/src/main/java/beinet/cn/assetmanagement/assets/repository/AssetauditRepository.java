package beinet.cn.assetmanagement.assets.repository;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetauditRepository extends JpaRepository<Assetaudit, Integer> {
    List<Assetaudit> findAllByAccountAndTypeInOrderByIdDesc(String account, List<String> type);
}
