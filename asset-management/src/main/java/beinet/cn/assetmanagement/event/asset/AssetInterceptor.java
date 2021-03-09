package beinet.cn.assetmanagement.event.asset;

import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import org.springframework.stereotype.Component;

@Component
public class AssetInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.EditAsset) || log.getType().equals(OperateEnum.AddAsset);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        AssetsDto dto = (AssetsDto) source;
        log.setCode(String.valueOf(dto.getId()));
        String description = String.format("class:%s,name:%s,desc:%s,place:%s,price:%s,state:%s,user:%s,time:%s",
                dto.getClassId(),
                dto.getAssetName(),
                dto.getDescription(),
                dto.getPlace(),
                dto.getPrice(),
                dto.getState(),
                dto.getUserName(),
                dto.getAccountTime());
        log.setDescription(description);
    }
}
