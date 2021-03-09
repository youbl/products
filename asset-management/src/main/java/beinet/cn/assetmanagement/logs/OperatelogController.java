package beinet.cn.assetmanagement.logs;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.service.OperatelogService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OperatelogController {
    private final OperatelogService operatelogService;

    public OperatelogController(OperatelogService operatelogService) {
        this.operatelogService = operatelogService;
    }

    @GetMapping("operatelogs")
    public List<Operatelog> findAll() {
        return operatelogService.findAll();
    }

    @GetMapping("operatelog")
    public Operatelog findById(@RequestParam Integer id) {
        return operatelogService.findById(id);
    }
    
}
