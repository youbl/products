package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import org.springframework.stereotype.Component;

@Component
public class LogInterceptorDefault implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return false;
    }

    @Override
    public void filter(Operatelog log, Object source) {

    }
}
