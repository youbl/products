package beinet.cn.assetmanagement.logs.repository;

import beinet.cn.assetmanagement.logs.model.Configs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigsRepository extends JpaRepository<Configs, Integer> {
    List<Configs> findByTypeInOrderByTypeAscCodeAsc(List<String> type);
}
