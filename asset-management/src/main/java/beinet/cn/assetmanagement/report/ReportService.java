package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import beinet.cn.assetmanagement.report.dto.TotalDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    private final AssetsRepository assetsRepository;

    public ReportService(AssetsRepository assetsRepository) {
        this.assetsRepository = assetsRepository;
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
        String mainKey;
        String subKey;
        if (isState) {
            mainKey = "state";
            subKey = "classId";
        } else {
            mainKey = "classId";
            subKey = "state";
        }
        List<Map<String, Object>> result = assetsRepository.totalByStateAndClass();
        return mergeData(result, mainKey, subKey);
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
        String mainKey;
        String subKey;
        if (isDepart) {
            mainKey = "department";
            subKey = "classId";
        } else {
            mainKey = "classId";
            subKey = "department";
        }
        List<Map<String, Object>> result = assetsRepository.totalByDepartment();
        return mergeData(result, mainKey, subKey);
    }

    private Collection<TotalDto> mergeData(List<Map<String, Object>> result,
                                           String mainKey, String subKey) {
        Map<Integer, TotalDto> ret = new HashMap<>();

        for (Map<String, Object> row : result) {
            int val = (int) row.get(mainKey);
            TotalDto dto = ret.get(val);
            if (dto == null) {
                dto = new TotalDto();
                dto.setKey(val);
                dto.setDetails(new ArrayList<>());
                ret.put(val, dto);
            }

            int subVal = (int) row.get(subKey);
            int num = Integer.parseInt(row.get("num").toString()); // 避免异常1: "java.math.BigInteger cannot be cast to java.lang.Integer"

            TotalDto subdto = new TotalDto();
            subdto.setKey(subVal);
            subdto.setNum(num);

            dto.getDetails().add(subdto);
            dto.setNum(dto.getNum() + num);
        }
        return ret.values();
    }
}
