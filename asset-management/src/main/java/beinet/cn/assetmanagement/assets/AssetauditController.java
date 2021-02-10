package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.model.AssetauditDto;
import beinet.cn.assetmanagement.assets.service.AssetauditService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetauditController {
    private final AssetauditService assetauditService;

    public AssetauditController(AssetauditService assetauditService) {
        this.assetauditService = assetauditService;
    }

    @GetMapping("/assetaudits")
    public List<Assetaudit> findAll(AuthDetails details) {
        return assetauditService.findAll(details.getAccount());
    }

    @GetMapping("/assetaudit")
    public Assetaudit findById(@RequestParam Integer id) {
        return assetauditService.findById(id);
    }

    /**
     * 设置id为0，以确保只能新增审核
     *
     * @param item
     * @param details
     * @return
     */
    @PostMapping("/assetaudit")
    public Assetaudit save(@RequestBody AssetauditDto item, AuthDetails details) {
        if (item == null) {
            return null;
        }
        item.setAccount(details.getAccount());
        item.setState(0);
        item.setId(0);
        return assetauditService.save(item.mapTo());
    }

    @PostMapping("/assetaudit/cancel/{id}")
    public void cancelAudit(@PathVariable int id, AuthDetails details) {
        assetauditService.cancelAudit(id, details.getAccount());
    }
}
