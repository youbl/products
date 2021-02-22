package beinet.cn.assetmanagement.report.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditDetailDto {
    private String account;
    private String userName;
    private String description;
    private String code;
    private String assetName;
    private LocalDateTime creationTime;
}
