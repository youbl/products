package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.model.Users;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * AuthDetails
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:55
 */
@Data
public class AuthDetails {
    private String account;

    private String userAgent;

    public String getAccount() {
        if (StringUtils.isEmpty(account)) {
            return "匿名";
        }
        return account;
    }
}
