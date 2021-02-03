package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.assets.service.AssetsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AssetsController {
    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @GetMapping("assets")
    public List<Assets> findAll() {
        return assetsService.findAll();
    }

    @GetMapping("asset")
    public Assets findById(Integer id) {
        return assetsService.findById(id);
    }

    @PostMapping("asset")
    public Assets save(@RequestBody AssetsDto item) {
        if (item == null) {
            return null;
        }
        return assetsService.save(item.mapTo());
    }

}
