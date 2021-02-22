package beinet.cn.assetmanagement.assets;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.model.AssetauditDetail;
import beinet.cn.assetmanagement.assets.model.AssetauditDto;
import beinet.cn.assetmanagement.assets.service.AssetauditService;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AssetauditController {
    private final AssetauditService assetauditService;

    public AssetauditController(AssetauditService assetauditService) {
        this.assetauditService = assetauditService;
    }

    @GetMapping("/assetaudits/{auditType}")
    public List<Assetaudit> findAll(@PathVariable String auditType, AuthDetails details) {
        return assetauditService.findAuditByType(details.getAccount(), auditType);
    }

    @GetMapping("/assetaudits/admin/{auditType}")
    public List<Assetaudit> findForAdmin(@PathVariable String auditType, AuthDetails details) {
        return assetauditService.findForAdmin(details.getAccount(), auditType);
    }

    @GetMapping("/assetauditDetails/{auditId}")
    public List<AssetauditDetail> findAll(@PathVariable int auditId) {
        List<AssetauditDetail> result = assetauditService.findAllDetails(auditId);
        return result;
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
    public void save(@RequestBody AssetauditDto item, AuthDetails details) {
        if (item == null) {
            return;
        }
        item.setAccount(details.getAccount());
        assetauditService.newAudit(item);
    }

    @PostMapping("/assetaudit/cancel/{id}")
    public void cancelAudit(@PathVariable int id, AuthDetails details) {
        assetauditService.cancelAudit(id, details.getAccount());
    }

    @PostMapping("/assetaudit/audit")
    public void doAudit(@RequestBody AssetauditDto item, AuthDetails details) {
        if (item == null) {
            return;
        }
        item.setAuditUser(details.getAccount());
        assetauditService.doAudit(item);
    }

}
