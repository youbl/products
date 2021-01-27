package beinet.cn.assetmanagement.user.repository;

import beinet.cn.assetmanagement.user.entity.ValidCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ValidCodeRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/27 22:58
 */
public interface ValidCodeRepository extends JpaRepository<ValidCode, Integer> {
    /**
     * 根据序号和状态，查找第一条数据
     *
     * @param sn     序号
     * @param errNum 允许错误次数
     * @return 记录
     */
    ValidCode findTopBySnAndEnableErrNumIsGreaterThan(String sn, int errNum);
}
