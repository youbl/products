package beinet.cn.assetmanagement.user;

import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.service.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("department")
    public Department save(@RequestBody Department item) {
        if (item == null) {
            return null;
        }
        return departmentService.save(item);
    }

}
