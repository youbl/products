package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.model.Users;
import lombok.Data;

/**
 * AuthDetails
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:55
 */
@Data
public class AuthDetails {
    private Users user;

    private String userAgent;

    public String getAccount() {
        return user == null ? "匿名" : user.getAccount();
    }
}
