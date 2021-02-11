package beinet.cn.assetmanagement.assets.repository;

import beinet.cn.assetmanagement.assets.model.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    List<Assets> findAllByAccount(String adminUserName);

    Assets findByCode(String code);

    List<Assets> findAllByCodeIn(List<String> codes);
}
