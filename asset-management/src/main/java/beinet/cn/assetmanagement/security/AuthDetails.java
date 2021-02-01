package beinet.cn.assetmanagement.security;

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
    private String account;
    private String role;

    private String userAgent;
}
