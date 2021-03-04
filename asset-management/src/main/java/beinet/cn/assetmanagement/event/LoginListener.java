package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.service.OperatelogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoginListener extends BaseListener {

    public LoginListener(OperatelogService operatelogService) {
        super(operatelogService);
    }

    /**
     * 接收登录事件
     */
    @EventListener
    public void handleLogin(LoginDto dto) {
        Operatelog log = Operatelog.builder()
                .type("Login")
                .subType("Success")
                .account(dto.getAccount())
                .description(dto.getIp())
                .build();
        addLog(log);
    }
}
