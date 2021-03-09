package beinet.cn.assetmanagement.event.user;

import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.user.model.UsersDto;
import org.springframework.stereotype.Component;

@Component
public class AdminInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.AdminAdd) || log.getType().equals(OperateEnum.AdminModify);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        UsersDto dto = (UsersDto) source;
        log.setAccount(dto.getAccount());

        String description = String.format("name:%s,code:%s,depart:%s,state:%s,role:%s",
                dto.getUserName(),
                dto.getCode(),
                dto.getDepartment(),
                dto.getState(),
                dto.getRoles());
        log.setDescription(description);
    }
}
