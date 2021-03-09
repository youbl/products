package beinet.cn.assetmanagement.event.user;

import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.user.model.UsersDto;
import org.springframework.stereotype.Component;

@Component
public class RegisterInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.Register) || log.getType().equals(OperateEnum.Modify);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        UsersDto dto = (UsersDto) source;
        log.setAccount(dto.getAccount());
        String description = String.format("name:%s,code:%s,depart:%s",
                dto.getUserName(),
                dto.getCode(),
                dto.getDepartment());
        log.setDescription(description);
    }
}
