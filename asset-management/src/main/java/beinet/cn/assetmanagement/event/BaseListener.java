package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.service.OperatelogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseListener {
    protected final List<LogInterceptor> handlers;
    protected final OperatelogService operatelogService;

    public BaseListener(OperatelogService operatelogService,
                        List<LogInterceptor> handlers) {
        this.operatelogService = operatelogService;
        this.handlers = handlers;
    }

    @EventListener
    public void handleEvents(EventDto dto) {
        Operatelog log = Operatelog.builder()
                .type(dto.getType())
                .account(dto.getAccount())
                .ip(dto.getIp())
                .operator(dto.getAccount())
                .build();

        for (LogInterceptor interceptor : handlers) {
            if (interceptor.isSupported(log)) {
                interceptor.filter(log, dto.getSource());
            }
        }
        addLog(log);
    }

    protected void addLog(Operatelog log) {
        operatelogService.save(log);
    }
}
