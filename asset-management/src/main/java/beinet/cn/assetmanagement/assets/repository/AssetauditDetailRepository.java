package beinet.cn.assetmanagement.assets.repository;

import beinet.cn.assetmanagement.assets.model.AssetauditDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetauditDetailRepository extends JpaRepository<AssetauditDetail, Integer> {
    List<AssetauditDetail> findAllByAuditIdOrderById(int auditId);
}
