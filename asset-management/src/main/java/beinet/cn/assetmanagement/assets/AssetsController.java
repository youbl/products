package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.model.AssetsDto;
import beinet.cn.assetmanagement.assets.service.AssetsService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetsController {
    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @GetMapping("assets")
    public List<Assets> findAll(AuthDetails details) {
        return assetsService.findAll(details.getAccount());
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

    /**
     * 扫码接口，不需要登录
     *
     * @param code 二维码
     * @return 资产
     */
    @GetMapping("/assetCode/{code}")
    public Assets findByCode(@PathVariable String code) {
        return assetsService.findByCode(code);
    }
}
