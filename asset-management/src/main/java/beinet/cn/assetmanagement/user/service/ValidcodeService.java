package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.Validcode;
import beinet.cn.assetmanagement.user.repository.ValidcodeRepository;
import beinet.cn.assetmanagement.utils.StrHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ValidcodeService {
    private final ValidcodeRepository validcodeRepository;

    public ValidcodeService(ValidcodeRepository validcodeRepository) {
        this.validcodeRepository = validcodeRepository;
    }

    public List<Validcode> findAll() {
        return validcodeRepository.findAll();
    }

    public Validcode findById(Integer id) {
        return validcodeRepository.findById(id).orElse(null);
    }

    public Validcode save(Validcode item) {
        if (item == null) {
            return null;
        }
        return validcodeRepository.save(item);
    }



    /**
     * 插入新的验证码
     *
     * @return 验证码对象
     */
    public Validcode addCodeAndGetSn() {
        String code = StrHelper.getRndStr(4, 0);
        String sn = StrHelper.getRndStr(6, 0);
        Validcode record = Validcode.builder()
                .code(code)
                .sn(sn)
                .type(0)
                .enableErrNum(1)
                .build();
        validcodeRepository.save(record);
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
        Validcode record = validcodeRepository.findTopBySnAndEnableErrNumIsGreaterThan(sn, 0);
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
        validcodeRepository.save(record);
        return ret;
    }

}
