package beinet.cn.assetmanagement.event.asset;

import beinet.cn.assetmanagement.assets.model.AssetclassDto;
import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.user.model.Department;
import org.springframework.stereotype.Component;

@Component
public class ClassInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.EditAssetClass) || log.getType().equals(OperateEnum.AddAssetClass);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        AssetclassDto dto = (AssetclassDto) source;
        log.setCode(String.valueOf(dto.getId()));
        String description = String.format("name:%s,desc:%s,admin:%s",
                dto.getClassName(),
                dto.getDescription(),
                dto.getAdmin());
        log.setDescription(description);
    }
}
