package beinet.cn.assetmanagement.event.department;

import beinet.cn.assetmanagement.event.LogInterceptor;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.model.UsersDto;
import org.springframework.stereotype.Component;

@Component
public class DepartInterceptor implements LogInterceptor {
    @Override
    public boolean isSupported(Operatelog log) {
        return log.getType().equals(OperateEnum.EditDepartment) || log.getType().equals(OperateEnum.AddDepartment);
    }

    @Override
    public void filter(Operatelog log, Object source) {
        Department dto = (Department) source;
        log.setCode(String.valueOf(dto.getId()));
        String description = String.format("name:%s,desc:%s",
                dto.getDepartmentName(),
                dto.getDescription());
        log.setDescription(description);
    }
}
