package beinet.cn.assetmanagement.user.repository;

import beinet.cn.assetmanagement.user.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findAllByOrderByDepartmentNameAsc();

    Department findByDepartmentName(String name);
}
