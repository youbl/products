package beinet.cn.assetmanagement.utils;

import org.springframework.util.DigestUtils;

/**
 * SecurityHelper
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/17 9:22
 */
public final class SecurityHelper {
    public static String md5(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            throw new RuntimeException("md5参数不能为空");
        }
        String useStr = String.valueOf(objArr[0]);
        if (objArr.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (Object item : objArr) {
                sb.append(item).append('-');
            }
            // 去除多余的 -
            useStr = sb.toString().substring(0, sb.length() - 1);
        }
        if (useStr.length() <= 0) {
            throw new RuntimeException("md5参数全部为空");
        }
        return DigestUtils.md5DigestAsHex(useStr.getBytes());
    }

}
