package beinet.cn.assetmanagement.logs;

import beinet.cn.assetmanagement.event.EventDto;
import beinet.cn.assetmanagement.event.Publisher;
import beinet.cn.assetmanagement.logs.model.Configs;
import beinet.cn.assetmanagement.logs.model.ConfigsDto;
import beinet.cn.assetmanagement.logs.model.OperateEnum;
import beinet.cn.assetmanagement.logs.service.ConfigsService;
import beinet.cn.assetmanagement.security.AuthDetails;
import beinet.cn.assetmanagement.utils.RequestHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Configs save(@RequestBody ConfigsDto item,
                        AuthDetails details,
                        HttpServletRequest request) {
        if (item == null) {
            return null;
        }
        Configs ret = configsService.save(item.mapTo());

        // 发事件
        Publisher.publishEvent(EventDto.builder()
                .type((item.getId() > 0) ? OperateEnum.EditConfig : OperateEnum.AddConfig)
                .account(details.getAccount())
                .ip(RequestHelper.getFullIP(request))
                .source(item)
                .build());
        return ret;
    }

}
