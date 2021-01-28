package beinet.cn.assetmanagement.user.service;

import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Integer id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department save(Department item) {
        if (item == null) {
            return null;
        }
        return departmentRepository.save(item);
    }

}
