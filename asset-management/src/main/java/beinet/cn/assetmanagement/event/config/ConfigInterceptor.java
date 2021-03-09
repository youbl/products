package beinet.cn.assetmanagement.event.config;

import beinet.cn.assetmanagement.assets.model.AssetclassDto;
import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.ConfigsDto;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import org.springframework.stereotype.Component;

@Component
public class ConfigInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.EditConfig) || log.getType().equals(OperateEnum.AddConfig);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        ConfigsDto dto = (ConfigsDto) source;
        log.setCode(String.valueOf(dto.getId()));
        String description = String.format("type:%s,code:%s,desc:%s",
                dto.getType(),
                dto.getCode(),
                dto.getDescription());
        log.setDescription(description);
    }
}
