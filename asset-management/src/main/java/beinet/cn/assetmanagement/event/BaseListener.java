package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.service.OperatelogService;

public abstract class BaseListener {
    protected final OperatelogService operatelogService;

    public BaseListener(OperatelogService operatelogService) {
        this.operatelogService = operatelogService;
    }

    protected void addLog(Operatelog log) {
        operatelogService.save(log);
    }
}
