package beinet.cn.assetmanagement.logs;

import beinet.cn.assetmanagement.logs.model.Operatelog;
import beinet.cn.assetmanagement.logs.service.OperatelogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public Operatelog findById(Integer id) {
        return operatelogService.findById(id);
    }

    @PostMapping("operatelog")
    public Operatelog save(@RequestBody Operatelog item) {
        if (item == null) {
            return null;
        }
        return operatelogService.save(item);
    }

}
