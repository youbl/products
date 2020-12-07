package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.repository.QrCodeRepository;
import com.chaoip.ipproxy.repository.entity.QrCode;
import org.springframework.stereotype.Service;

/**
 * 二维码服务类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:57
 */
@Service
public class QrCodeService {
    private final QrCodeRepository qrCodeRepository;

    public QrCodeService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    /**
     * 插入一个二维码
     *
     * @param code 阿里地址
     * @return 结果
     */
    public QrCode addQrCode(QrCode code) {
        return qrCodeRepository.save(code);
    }

    public QrCode findByName(String name) {
        return qrCodeRepository.findTopByNameOrderByCreationTimeDesc(name);
    }

    public QrCode findByOrder(String orderNo) {
        return qrCodeRepository.findTopByOrderNo(orderNo);
    }
}
