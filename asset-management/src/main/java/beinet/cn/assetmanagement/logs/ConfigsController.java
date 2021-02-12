package beinet.cn.assetmanagement.logs;

import beinet.cn.assetmanagement.logs.model.Configs;
import beinet.cn.assetmanagement.logs.model.ConfigsDto;
import beinet.cn.assetmanagement.logs.service.ConfigsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConfigsController {
    private final ConfigsService configsService;

    public ConfigsController(ConfigsService configsService) {
        this.configsService = configsService;
    }

    @GetMapping("configses")
    public List<Configs> findAll() {
        return configsService.findAll();
    }

    @GetMapping("/configses/{type}")
    public List<Configs> findAll(@PathVariable String type) {
        return configsService.findAll(type);
    }

    @GetMapping("configs")
    public Configs findById(@RequestParam Integer id) {
        return configsService.findById(id);
    }

    @PostMapping("configs")
    public Configs save(@RequestBody ConfigsDto item) {
        if (item == null) {
            return null;
        }
        return configsService.save(item.mapTo());
    }

}
