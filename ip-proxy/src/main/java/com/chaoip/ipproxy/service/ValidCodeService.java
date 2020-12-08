package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.SmsDto;
import com.chaoip.ipproxy.repository.ValidCodeRepository;
import com.chaoip.ipproxy.repository.entity.ValidCode;
import com.chaoip.ipproxy.util.SmsHelper;
import com.chaoip.ipproxy.util.StrHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 验证码服务类
 */
@Service
@Slf4j
public class ValidCodeService {
    private final ValidCodeRepository validCodeRepository;
    private final SmsHelper smsHelper;

    public ValidCodeService(ValidCodeRepository validCodeRepository, SmsHelper smsHelper) {
        this.validCodeRepository = validCodeRepository;
        this.smsHelper = smsHelper;
    }

    /**
     * 插入新的验证码
     *
     * @return 验证码对象
     */
    public ValidCode addImgCodeAndGetSn() {
        return addCodeAndGetSn(null);
    }

    /**
     * 插入新的验证码
     *
     * @param phone 类
     * @return 验证码对象
     */
    public ValidCode addCodeAndGetSn(String phone) {
        int type; // 型，0图形验证码，1为短信验证码
        int enableErrNum;
        if (StringUtils.isEmpty(phone)) {
            type = 0;
            enableErrNum = 1;
        } else {
            if (checkSmsSended(phone)) {
                throw new RuntimeException("该手机1分钟内发过短信:" + phone);
            }
            type = 1;
            enableErrNum = 3;
        }
        String code = StrHelper.getRndStr(4, type);
        String sn = StrHelper.getRndStr(6, 0);
        ValidCode record = ValidCode.builder()
                .code(code)
                .sn(sn)
                .type(type)
                .enableErrNum(enableErrNum)
                .expireTime(LocalDateTime.now().plusSeconds(300))
                .build();
        validCodeRepository.save(record);
        return record;
    }

    private boolean checkSmsSended(String phone) {
        // todo 1分钟内是否发过短信
        return false;
    }

    /**
     * 校验图形码，并发送短信验证码
     *
     * @param dto 参数
     * @return 验证码序号
     */
    public String sendSmsCode(SmsDto dto) throws JsonProcessingException {
        // 获取一个新的验证码
        ValidCode code = addCodeAndGetSn(dto.getPhone());
        // 发短信
        String result = smsHelper.send(dto.getPhone(), code.getCode());

        StringBuilder sbMsg = new StringBuilder();
        sbMsg.append("发验证码:")
                .append(dto.getPhone())
                .append(" code:")
                .append(code.getCode())
                .append(" 结果:")
                .append(result);

        if (!result.contains("\"Code\":\"OK\"")) {
            log.error(sbMsg.toString());
            throw new RuntimeException("短信发送失败");
        }
        log.info(sbMsg.toString());
        return code.getSn();
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
