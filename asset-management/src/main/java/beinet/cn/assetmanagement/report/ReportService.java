package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.repository.AssetclassRepository;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.report.dto.TotalDto;
import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    private final AssetsRepository assetsRepository;
    private final AssetclassRepository assetClassRepository;
    private final DepartmentRepository departmentRepository;

    public ReportService(AssetsRepository assetsRepository,
                         AssetclassRepository assetClassRepository,
                         DepartmentRepository departmentRepository) {
        this.assetsRepository = assetsRepository;
        this.assetClassRepository = assetClassRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * 按状态汇总全部 或 按分类汇总全部
     *
     * @param type 按状态还是按分类
     * @return 数据
     */
    public Collection<TotalDto> totalByStateAndClass(String type) {
        return totalAllByField(type != null && type.equals("state"));
    }

    private Collection<TotalDto> totalAllByField(boolean isState) {
        List<Assetclass> classList = assetClassRepository.findAll();

        String mainKey;
        String subKey;
        NameGetter mainGetter;
        NameGetter subGetter;
        if (isState) {
            mainKey = "state";
            subKey = "classId";
            mainGetter = key -> getStateName((int) key);
            subGetter = key -> getClassName((int) key, classList);
        } else {
            mainKey = "classId";
            subKey = "state";
            mainGetter = key -> getClassName((int) key, classList);
            subGetter = key -> getStateName((int) key);
        }
        List<Map<String, Object>> result = assetsRepository.totalByStateAndClass();
        return mergeData(result, mainKey, subKey, mainGetter, subGetter);
    }

    /**
     * 按部门汇总借出资产 或 按分类汇总借出资产
     *
     * @param type 按部门还是按分类
     * @return 数据
     */
    public Collection<TotalDto> totalByDepart(String type) {
        return totalDepartByField(type != null && type.equals("depart"));
    }

    private Collection<TotalDto> totalDepartByField(boolean isDepart) {
        List<Department> departmentList = departmentRepository.findAll();
        List<Assetclass> classList = assetClassRepository.findAll();

        String mainKey;
        String subKey;
        NameGetter mainGetter;
        NameGetter subGetter;
        if (isDepart) {
            mainKey = "department";
            subKey = "classId";
            mainGetter = key -> getDepName((int) key, departmentList);
            subGetter = key -> getClassName((int) key, classList);
        } else {
            mainKey = "classId";
            subKey = "department";
            mainGetter = key -> getClassName((int) key, classList);
            subGetter = key -> getDepName((int) key, departmentList);
        }
        List<Map<String, Object>> result = assetsRepository.totalByDepartment();
        return mergeData(result, mainKey, subKey, mainGetter, subGetter);
    }

    private Collection<TotalDto> mergeData(List<Map<String, Object>> result,
                                           String mainKey, String subKey,
                                           NameGetter mainGetter, NameGetter subGetter) {
        Map<Integer, TotalDto> ret = new HashMap<>();

        for (Map<String, Object> row : result) {
            int val = (int) row.get(mainKey);
            TotalDto dto = ret.get(val);
            if (dto == null) {
                dto = new TotalDto();
                dto.setKey(val);
                dto.setDetails(new ArrayList<>());
                dto.setName(mainGetter.getName(val));
                ret.put(val, dto);
            }

            int subVal = (int) row.get(subKey);
            int num = Integer.parseInt(row.get("num").toString()); // 避免异常1: "java.math.BigInteger cannot be cast to java.lang.Integer"

            TotalDto subdto = new TotalDto();
            subdto.setKey(subVal);
            subdto.setName(subGetter.getName(subVal));
            subdto.setNum(num);

            dto.getDetails().add(subdto);
            dto.setNum(dto.getNum() + num);
        }
        return ret.values();
    }

    private String getClassName(int classId, List<Assetclass> classList) {
        for (Assetclass css : classList) {
            if (css.getId() == classId) {
                return css.getClassName();
            }
        }
        return String.valueOf(classId);
    }

    private String getDepName(int depId, List<Department> departmentList) {
        for (Department css : departmentList) {
            if (css.getId() == depId) {
                return css.getDepartmentName();
            }
        }
        return String.valueOf(depId);
    }

    private String getStateName(int state) {
        List<String> arrState = new ArrayList<>();
        arrState.add("库存");
        arrState.add("借出");
        arrState.add("故障");
        arrState.add("报废");

        return arrState.get(state);
    }

    interface NameGetter {
        String getName(Object key);
    }
}
