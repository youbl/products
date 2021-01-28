package beinet.cn.assetmanagement.user.repository;

import beinet.cn.assetmanagement.user.model.Validcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidcodeRepository extends JpaRepository<Validcode, Integer> {
    /**
     * 根据序号和状态，查找第一条数据
     *
     * @param sn     序号
     * @param errNum 允许错误次数
     * @return 记录
     */
    Validcode findTopBySnAndEnableErrNumIsGreaterThan(String sn, int errNum);
}
