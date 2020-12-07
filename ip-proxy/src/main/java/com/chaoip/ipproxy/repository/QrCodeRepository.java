package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.QrCode;

/**
 * 二维码仓储类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface QrCodeRepository extends BaseRepository<QrCode, Long> {
    QrCode findTopByNameOrderByCreationTimeDesc(String name);

    QrCode findTopByOrderNo(String orderNo);
}
