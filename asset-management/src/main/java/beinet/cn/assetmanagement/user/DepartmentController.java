package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.event.EventDto;
import beinet.cn.assetmanagement.event.Publisher;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.security.AuthDetails;
import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.service.DepartmentService;
import beinet.cn.assetmanagement.utils.RequestHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("departments")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("department")
    public Department findById(Integer id) {
        return departmentService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("department")
    public Department save(@RequestBody Department item,
                           AuthDetails details,
                           HttpServletRequest request) {
        if (item == null) {
            return null;
        }
        Department ret = departmentService.save(item);
        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type((item.getId() > 0) ? OperateEnum.EditDepartment : OperateEnum.AddDepartment)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(item)
                .build());
        return ret;
    }

}
