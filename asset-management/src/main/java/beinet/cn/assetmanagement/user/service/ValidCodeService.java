package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.entity.ValidCode;
import beinet.cn.assetmanagement.user.repository.ValidCodeRepository;
import beinet.cn.assetmanagement.utils.StrHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * ValidCodeService
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/27 22:59
 */
@Service
public class ValidCodeService {
    private final ValidCodeRepository validCodeRepository;

    public ValidCodeService(ValidCodeRepository validCodeRepository) {
        this.validCodeRepository = validCodeRepository;
    }

    /**
     * 插入新的验证码
     *
     * @return 验证码对象
     */
    public ValidCode addCodeAndGetSn() {
        String code = StrHelper.getRndStr(4, 0);
        String sn = StrHelper.getRndStr(6, 0);
        ValidCode record = ValidCode.builder()
                .code(code)
                .sn(sn)
                .type(0)
                .enableErrNum(1)
                .build();
        validCodeRepository.save(record);
        return record;
    }

    /**
     * 验证码怀序号是否匹配，并且是未验证
     *
     * @param code 用户输入的验证码
     * @param sn   序号
     * @return 是否匹配
     */
    @Transactional
    public boolean validByCodeAndSn(String code, String sn) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(sn)) {
            return false;
        }
        ValidCode record = validCodeRepository.findTopBySnAndEnableErrNumIsGreaterThan(sn, 0);
        if (record == null) {
            return false;
        }
        boolean ret = (record.getCode().equalsIgnoreCase(code));
        if (ret) {
            // 成功时，验证码要作废
            record.setEnableErrNum(0);
        } else {
            // 失败时，验证码可用次数减1
            record.setEnableErrNum(record.getEnableErrNum() - 1);
        }
        validCodeRepository.save(record);
        return ret;
    }
}
