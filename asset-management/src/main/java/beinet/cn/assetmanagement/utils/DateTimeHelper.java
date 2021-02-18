package beinet.cn.assetmanagement.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateTimeHelper {
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
    public static final long DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;

    public static final String FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern(FORMAT1);
    public static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern(FORMAT2);

    public static final LocalDateTime MIN = LocalDateTime.of(1900, 1, 1, 0, 0);

    /**
     * 把字符串转换为LocalDateTime返回
     *
     * @param str 日期字符串
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new RuntimeException("参数不能为空");
        }
        if (str.indexOf(' ') <= 0 && str.length() <= 13) {
            str += " 00:00:00";
        }
        try {
            return LocalDateTime.parse(str, FORMATTER1);
        } catch (Exception exp) {
            return LocalDateTime.parse(str, FORMATTER2);
        }
    }


    /**
     * 格式化Excel时间
     *
     * @param dayAndMills 导入的Excel数值格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatExcelDate(Double dayAndMills) {
        Calendar calendar = new GregorianCalendar(1900, 0, -1);
        Calendar calendar2 = new GregorianCalendar(1900, Calendar.JANUARY, 1);

        int day = (int) Math.floor(dayAndMills);
        // 小数是占一天的总毫秒数的百分比
        int milliSeconds = (int) ((dayAndMills - day) * DAY_MILLISECONDS);
        calendar.add(Calendar.DATE, day);
        calendar.add(Calendar.MILLISECOND, milliSeconds);

        calendar2.add(Calendar.DATE, day);
        calendar2.add(Calendar.MILLISECOND, milliSeconds);

        SimpleDateFormat sdFormat = new SimpleDateFormat(FORMAT2);
        return sdFormat.format(calendar.getTime());
    }
}
