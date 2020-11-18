import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * IpAutoConfiguration
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 14:14
 */
@Configuration
public class IpAutoConfiguration {
    @Bean
    public ObjectMapper getMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeSerializerBeinet(getDateTimeFormatter()));

        return Jackson2ObjectMapperBuilder.json().modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }

    // 支持的格式有
    // "2020-12-31 23:58:59"
    // "2020-12-31T23:58:59"
    private DateTimeFormatter getDateTimeFormatter() {
        // return DateTimeFormatter.ofPattern("yyyy-MM-dd[[ ]['T']HH:mm:ss]");
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)  // 这个要求4位年，2位月，2位日
                .optionalStart().appendLiteral(' ').optionalEnd()       // ' ' made optional
                .optionalStart().appendLiteral('T').optionalEnd()       // 'T' made optional
                .optionalStart().append(ISO_LOCAL_TIME).optionalEnd()   // time made optional
                .optionalStart().appendOffsetId().optionalEnd() // zone and offset made optional
                .optionalStart().appendOffset("+HHMM", "Z").optionalEnd()                //为了兼容时区不带:这种格式

                .optionalStart()
                .appendLiteral('[')
                .parseCaseSensitive()
                .appendZoneRegionId()
                .appendLiteral(']')
                .optionalEnd()
                .toFormatter();
    }

    public class LocalDateTimeSerializerBeinet extends LocalDateTimeDeserializer {

        public LocalDateTimeSerializerBeinet(DateTimeFormatter formatter) {
            super(formatter);
        }

        // 增加 yyyy-MM-dd 的支持
        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
                String string = fixMonthAndDay(parser.getText().trim());
                if (!string.isEmpty()) {
                    return LocalDateTime.parse(string, this._formatter);
                }
            }
            return super.deserialize(parser, context);
        }

        /**
         * 如果月份只有1位，或日期只有1位，补零
         *
         * @param ymd 年月日
         * @return 带时间的完整日期字符串
         */
        private String fixMonthAndDay(String ymd) {
            int monthIdx = ymd.indexOf('-');
            if (monthIdx < 0)
                return "";
            int dayIdx = ymd.indexOf('-', monthIdx + 1);
            if (dayIdx < 0)
                return "";

            String year = ymd.substring(0, monthIdx);
            if (year.length() != 4)
                return ""; // 不是yyyy-MM-dd返回不处理

            String month = ymd.substring(monthIdx + 1, dayIdx);
            if (month.length() < 1 || month.length() > 2)
                return ""; // 不是yyyy-MM-dd返回不处理
            if (month.length() < 2)
                month = "0" + month;

            String day;

            String otherStr = ymd.substring(dayIdx + 1);
            if (otherStr.length() <= 1) {
                day = otherStr;
                otherStr = "";
            } else if ('1' <= otherStr.charAt(1) && otherStr.charAt(1) <= '9') {
                day = otherStr.substring(0, 2);
                otherStr = otherStr.substring(2);
            } else {
                day = otherStr.substring(0, 1);
                otherStr = otherStr.substring(1);
            }

            if (day.length() < 1 || day.length() > 2)
                return ""; // 不是yyyy-MM-dd返回不处理
            if (day.length() < 2)
                day = "0" + day;

            if (otherStr.isEmpty())
                otherStr = " 00:00:00";
            return String.format("%s-%s-%s%s", year, month, day, otherStr);
        }
    }
}
