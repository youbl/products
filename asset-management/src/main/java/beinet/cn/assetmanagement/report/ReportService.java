package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.repository.AssetauditRepository;
import beinet.cn.assetmanagement.assets.repository.AssetclassRepository;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.report.dto.AuditDetailDto;
import beinet.cn.assetmanagement.report.dto.TotalDto;
import beinet.cn.assetmanagement.user.model.Department;
import beinet.cn.assetmanagement.user.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {
    private final AssetsRepository assetsRepository;
    private final AssetclassRepository assetClassRepository;
    private final DepartmentRepository departmentRepository;
    private final AssetauditRepository assetauditRepository;

    public ReportService(AssetsRepository assetsRepository,
                         AssetclassRepository assetClassRepository,
                         DepartmentRepository departmentRepository,
                         AssetauditRepository assetauditRepository) {
        this.assetsRepository = assetsRepository;
        this.assetClassRepository = assetClassRepository;
        this.departmentRepository = departmentRepository;
        this.assetauditRepository = assetauditRepository;
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

    public Collection<TotalDto> userNumRank() {
        List<TotalDto> ret = new ArrayList<>();

        List<Map<String, Object>> result = assetsRepository.totalByUserNum(10);
        for (Map<String, Object> row : result) {
            TotalDto dto = new TotalDto();
            ret.add(dto);
            dto.setKey(row.get("account").toString());
            dto.setName(row.get("userName").toString());
            dto.setNum(Integer.parseInt(row.get("cnt").toString()));
        }
        return ret;
    }

    public Collection<TotalDto> auditNumRank() {
        List<TotalDto> ret = new ArrayList<>();

        List<Map<String, Object>> result = assetsRepository.totalByAuditAssetNum(10);
        for (Map<String, Object> row : result) {
            TotalDto dto = new TotalDto();
            ret.add(dto);
            String type = row.get("type").toString();
            String subtype = row.get("subtype").toString();

            dto.setKey(type + "-" + subtype);
            dto.setName(row.get("description").toString());
            dto.setNum(Integer.parseInt(row.get("cnt").toString()));
        }
        return ret;
    }

    /**
     * 根据类型和子类型，查找审核明细资产返回
     *
     * @param type    类型
     * @param subType 子类型
     * @return 数据
     */
    public List<AuditDetailDto> findByType(String type, String subType) {
        List<Map<String, Object>> data = assetauditRepository.findDetailByType(type, subType);
        List<AuditDetailDto> ret = new ArrayList<>();
        for (Map<String, Object> row : data) {
            ret.add(AuditDetailDto
                    .builder()
                    .account(row.get("account").toString())
                    .userName(row.get("userName").toString())
                    .description(row.get("description").toString())
                    .code(row.get("code").toString())
                    .assetName(row.get("assetName").toString())
                    .creationTime(((Timestamp) row.get("creationTime")).toLocalDateTime())
                    .build());
        }
        return ret;
    }

    interface NameGetter {
        String getName(Object key);
    }
}
