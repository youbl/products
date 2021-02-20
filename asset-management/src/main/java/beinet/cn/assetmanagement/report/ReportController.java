package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.report.dto.TotalDto;
import beinet.cn.assetmanagement.utils.EasyExcelOperator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
            doExport(response, ret, type);
        }
        return ret;
    }

    private void doExport(HttpServletResponse response, Collection<TotalDto> ret, String type) throws Exception {
        List<List<String>> result = new ArrayList<>();
        // 标题行
        List<String> titles = new ArrayList<>();
        result.add(titles);
        titles.add(getTypeName(type));
        titles.add("数量");

        for (TotalDto dto : ret) {
            List<String> item = new ArrayList<>();
            result.add(item);
            item.add(dto.getName());
            item.add(String.valueOf(dto.getNum()));

            for (TotalDto subDto : dto.getDetails()) {
                List<String> subItem = new ArrayList<>();
                result.add(subItem);
                subItem.add("- " + subDto.getName());
                subItem.add(String.valueOf(subDto.getNum()));
            }
            // 空行
            item = new ArrayList<>();
            result.add(item);
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
        }
        return type;
    }
}
