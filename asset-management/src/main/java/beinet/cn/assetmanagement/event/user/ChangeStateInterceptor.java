package beinet.cn.assetmanagement.event.user;

import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.user.model.Users;
import org.springframework.stereotype.Component;

@Component
public class ChangeStateInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.AdminChgState);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        Users user = (Users) source;
        log.setAccount(user.getAccount());

        String description = user.getState() == 8 ? "启用" : "禁用";
        log.setDescription(description);
    }
}
