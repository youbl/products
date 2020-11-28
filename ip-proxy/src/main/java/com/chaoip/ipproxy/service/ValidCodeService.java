package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.repository.ValidCodeRepository;
import com.chaoip.ipproxy.repository.entity.ValidCode;
import com.chaoip.ipproxy.util.StrHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

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
        String code = StrHelper.getRndStr(4);
        String sn = StrHelper.getRndStr(6);
        ValidCode record = ValidCode.builder()
                .code(code)
                .sn(sn)
                .status(0)
                .expireTime(LocalDateTime.now().plusSeconds(300))
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
    public boolean validByCodeAndSn(String code, String sn) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(sn)) {
            return false;
        }
        ValidCode record = validCodeRepository.findTopBySnAndStatus(sn, 0);
        if (record == null) {
            return false;
        }
        boolean ret = (record.getCode().equalsIgnoreCase(code));
        // 无论成败，验证码都要作废
        record.setStatus(ret ? 1 : 2);
        validCodeRepository.save(record);
        return ret;
    }
}
