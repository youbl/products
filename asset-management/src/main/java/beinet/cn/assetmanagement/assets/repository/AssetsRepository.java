package beinet.cn.assetmanagement.assets.repository;

import beinet.cn.assetmanagement.assets.model.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AssetsRepository extends JpaRepository<Assets, Integer> {
    /**
     * 根据分类管理员，查找他管理的所有资产
     *
     * @param adminUserName
     * @return
     */
    @Query(value = "select a.* from assets a, assetclass b " +
            "where a.classId=b.id and b.admin=?1 " +
            "order by a.id desc",
            nativeQuery = true)
    List<Assets> findAllByAdminAccount(String adminUserName);

    List<Assets> findAllByAccountAndStateOrderByAssetName(String account, int state);

    Assets findByCode(String code);

    List<Assets> findAllByCodeInOrderByClassIdAscAssetNameAsc(String[] codes);

    @Query(value = "SELECT a.state, a.classId, COUNT(1) num " +
            "FROM assets a " +
            "GROUP BY a.state, a.classId",
            nativeQuery = true)
    List<Map<String, Object>> totalByStateAndClass();

    @Query(value = "SELECT b.department, a.classId, COUNT(1) num " +
            "FROM assets a " +
            "INNER JOIN users b ON a.state=1 AND a.account=b.account " +
            "GROUP BY b.department, a.classId",
            nativeQuery = true)
    List<Map<String, Object>> totalByDepartment();
}
