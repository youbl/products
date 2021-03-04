package beinet.cn.assetmanagement.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    private String account;
    private String ip;
}
