package com.chaoip.ipproxy.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class DateHelper {
    /**
     * Mongo的条件只接收Date类型
     *
     * @param ldt
     * @return
     */
    public static Date toDate(LocalDateTime ldt) {
        ZoneId zoneId = ZoneId.systemDefault();

        return Date.from(ldt.atZone(zoneId).toInstant());
    }
}
