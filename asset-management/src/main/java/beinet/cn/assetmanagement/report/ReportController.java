package beinet.cn.assetmanagement.report;

import beinet.cn.assetmanagement.report.dto.TotalDto;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report/total")
    public Collection<TotalDto> total(@RequestParam String type) {
        if (StringUtils.isEmpty(type)) {
            return new ArrayList<>();
        }
        if (type.equals("state") || type.equals("class")) {
            return reportService.totalByStateAndClass(type);
        }
        if (type.equals("classGet") || type.equals("depart")) {
            return reportService.totalByDepart(type);
        }
        return new ArrayList<>();
    }
}
