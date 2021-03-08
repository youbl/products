package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.report.dto.AuditDetailDto;
import beinet.cn.assetmanagement.report.dto.TotalDto;
import beinet.cn.assetmanagement.utils.EasyExcelOperator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class ReportController {
    private final ReportService reportService;
    private final EasyExcelOperator excelOperator;

    public ReportController(ReportService reportService,
                            EasyExcelOperator excelOperator) {
        this.reportService = reportService;
        this.excelOperator = excelOperator;
    }

    /**
     * 资产统计接口
     *
     * @param type     类型
     * @param export   是否导出
     * @param response 响应对象
     * @return 数据
     * @throws Exception 异常
     */
    @GetMapping("/report/total")
    public Collection<TotalDto> total(@RequestParam String type,
                                      @RequestParam(required = false) String export,
                                      HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(type)) {
            return new ArrayList<>();
        }
        Collection<TotalDto> ret;
        if (type.equals("state") || type.equals("class")) {
            ret = reportService.totalByStateAndClass(type);
        } else if (type.equals("classGet") || type.equals("depart")) {
            ret = reportService.totalByDepart(type);
        } else {
            return new ArrayList<>();
        }
        if (StringUtils.hasText(export)) {
            doExportAssetsTotal(response, ret, type);
        }
        return ret;
    }

    @GetMapping("/report/userNum")
    public Collection<TotalDto> userNum(@RequestParam(required = false) String export,
                                        HttpServletResponse response) throws Exception {
        Collection<TotalDto> ret = reportService.userNumRank();
        if (StringUtils.hasText(export)) {
            doExportAssetsTotal(response, ret, "userRank");
        }
        return ret;
    }

    @GetMapping("/report/auditNum")
    public Collection<TotalDto> auditNum(@RequestParam(required = false) String export,
                                         HttpServletResponse response) throws Exception {
        Collection<TotalDto> ret = reportService.auditNumRank();
        if (StringUtils.hasText(export)) {
            doExportAssetsTotal(response, ret, "auditRank");
        }
        return ret;
    }

    @GetMapping("/report/audit/{type}/{subType}/{departmentId}")
    public List<AuditDetailDto> findByType(@PathVariable String type,
                                           @PathVariable String subType,
                                           @PathVariable int departmentId) {
        return reportService.findByType(type, subType, departmentId);
    }

    private void doExportAssetsTotal(HttpServletResponse response, Collection<TotalDto> ret, String type) throws Exception {
        List<List<String>> result = new ArrayList<>();
        // 标题行
        List<String> titles = new ArrayList<>();
        result.add(titles);
        titles.add(getTypeName(type));
        titles.add("名称");
        titles.add("数量");

        for (TotalDto dto : ret) {
            List<String> item = new ArrayList<>();
            result.add(item);
            item.add(dto.getKey().toString());
            item.add(dto.getName());
            item.add(String.valueOf(dto.getNum()));
            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                continue;
            }
            for (TotalDto subDto : dto.getDetails()) {
                List<String> subItem = new ArrayList<>();
                result.add(subItem);
                subItem.add("- " + subDto.getKey());
                subItem.add(subDto.getName());
                subItem.add(String.valueOf(subDto.getNum()));
            }
            // 空行
            item = new ArrayList<>();
            result.add(item);
            item.add(" ");
            item.add(" ");
            item.add(" ");
        }
        response.addHeader("Content-Disposition", "attachment; filename=\"assets.xlsx\"");
        excelOperator.writeExcel(response.getOutputStream(), result);
    }

    private String getTypeName(String type) {
        switch (type) {
            case "state":
                return "资产状态统计";
            case "class":
                return "资产分类统计";
            case "classGet":
                return "领用分类统计";
            case "depart":
                return "领用部门统计";
            case "userRank":
                return "用户资产数量排行";
            case "auditRank":
                return "借还理由排行";
        }
        return type;
    }
}
