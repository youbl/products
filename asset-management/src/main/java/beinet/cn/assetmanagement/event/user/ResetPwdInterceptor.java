package beinet.cn.assetmanagement.event.user;

import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import org.springframework.stereotype.Component;

@Component
public class ResetPwdInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.AdminResetPwd);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        log.setDescription(source.toString());
    }
}
