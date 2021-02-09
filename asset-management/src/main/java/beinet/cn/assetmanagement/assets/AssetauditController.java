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
    public List<Assetaudit> findAll() {
        return assetauditService.findAll();
    }

    @GetMapping("/assetaudit")
    public Assetaudit findById(@RequestParam Integer id) {
        return assetauditService.findById(id);
    }

    @PostMapping("/assetaudit")
    public Assetaudit save(@RequestBody AssetauditDto item, AuthDetails details) {
        if (item == null) {
            return null;
        }
        item.setAccount(details.getAccount());
        return assetauditService.save(item.mapTo());
    }

}
