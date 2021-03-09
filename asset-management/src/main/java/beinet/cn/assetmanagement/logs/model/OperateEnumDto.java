package beinet.cn.assetmanagement.logs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperateEnumDto {
    private String code;
    private String description;
}