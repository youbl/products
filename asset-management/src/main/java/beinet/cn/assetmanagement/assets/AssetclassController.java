package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.model.AssetclassDto;
import beinet.cn.assetmanagement.assets.service.AssetclassService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("assetClass")
    public Assetclass findById(Integer id) {
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

}
