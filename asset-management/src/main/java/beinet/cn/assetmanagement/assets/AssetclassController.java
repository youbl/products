package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.model.AssetclassDto;
import beinet.cn.assetmanagement.assets.service.AssetclassService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetclassController {
    private final AssetclassService assetclassService;

    public AssetclassController(AssetclassService assetclassService) {
        this.assetclassService = assetclassService;
    }

    @GetMapping("assetClasses")
    public List<Assetclass> findAll() {
        return assetclassService.findAll();
    }

    @GetMapping("/assetClass")
    public Assetclass findById(@RequestParam Integer id) {
        return assetclassService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("assetClass")
    public Assetclass save(@RequestBody AssetclassDto item) {
        if (item == null) {
            return null;
        }
        return assetclassService.save(item.mapTo());
    }

    /**
     * 获取当前登录用户管理的分类id
     *
     * @param details 当前登录用户
     * @return id或0
     */
    @GetMapping("assetClassByUser")
    public int getClassId(AuthDetails details) {
        return assetclassService.findByAccountAdmin(details.getAccount());
    }
}
