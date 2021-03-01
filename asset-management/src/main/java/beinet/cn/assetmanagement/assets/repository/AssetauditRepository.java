package beinet.cn.assetmanagement.assets.repository;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AssetauditRepository extends JpaRepository<Assetaudit, Integer>, JpaSpecificationExecutor<Assetaudit> {
    List<Assetaudit> findAllByAccountAndTypeInOrderByIdDesc(String account, List<String> type);

    List<Assetaudit> findAllByTypeInOrderByIdDesc(List<String> type);

    List<Assetaudit> findAllByClassIdAndTypeInOrderByIdDesc(int classId, List<String> type);

    @Query(value = "SELECT b.account, c.userName, b.description, a.code, d.assetName, a.creationTime " +
            "FROM assetauditdetail a " +
            "INNER JOIN assetaudit b ON a.auditId=b.id " +
            "INNER JOIN users c ON c.account=b.account " +
            "INNER JOIN assets d ON d.code=a.code " +
            "WHERE b.type=?1 AND b.subtype=?2 " +
            "ORDER BY userName,assetName ",
            nativeQuery = true)
    List<Map<String, Object>> findDetailByType(String type, String subType);
}
