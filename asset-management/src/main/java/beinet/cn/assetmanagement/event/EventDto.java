package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.OperateEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private OperateEnum type;
    private String account;
    private String ip;

    private Object source;
}
