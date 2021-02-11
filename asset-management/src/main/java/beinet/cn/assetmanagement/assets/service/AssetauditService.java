package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.model.AssetauditDetail;
import beinet.cn.assetmanagement.assets.model.AssetauditDto;
import beinet.cn.assetmanagement.assets.repository.AssetauditDetailRepository;
import beinet.cn.assetmanagement.assets.repository.AssetauditRepository;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetauditService {
    private final AssetauditRepository assetauditRepository;
    private final AssetauditDetailRepository assetauditDetailRepository;
    private final AssetsService assetsService;

    public AssetauditService(AssetauditRepository assetauditRepository,
                             AssetauditDetailRepository assetauditDetailRepository,
                             AssetsService assetsService) {
        this.assetauditRepository = assetauditRepository;
        this.assetauditDetailRepository = assetauditDetailRepository;
        this.assetsService = assetsService;
    }

    public List<Assetaudit> findAll(String account) {
        if (StringUtils.isEmpty(account) || account.equals("匿名")) {
            return new ArrayList<>();
        }
        return assetauditRepository.findAllByAccountOrderByIdDesc(account);
    }

    public Assetaudit findById(Integer id) {
        return assetauditRepository.findById(id).orElse(null);
    }

    public Assetaudit save(Assetaudit item) {
        if (item == null) {
            return null;
        }
        return assetauditRepository.save(item);
    }

    public void cancelAudit(int id, String account) {
        Assetaudit audit = findById(id, account);
        audit.setState(2);
        save(audit);
    }

    @Transactional
    public void doAudit(AssetauditDto item) {
        if (item.getState() != 1 && item.getState() != 8) {
            throw new RuntimeException("审核状态设置有误:" + item.getState());
        }
        Assetaudit audit = findById(item.getId(), item.getAccount());
        audit.setState(item.getState());
        audit.setAuditUser(item.getAuditUser());
        audit.setAuditReason(item.getAuditReason());
        save(audit);

        if (audit.getState() == 8) {
            for (String code : item.getAssetCodes()) {
                // 保存分配的资产
                AssetauditDetail detail = AssetauditDetail.builder()
                        .code(code)
                        .auditId(audit.getId())
                        .build();
                assetauditDetailRepository.save(detail);

                // 更新资产状态和归属人
                assetsService.setAssetsUser(code, audit.getAccount());
            }
        }
    }

    public List<AssetauditDetail> findAllDetails(int auditId, String account) {
        Assetaudit audit = findById(auditId, account);
        return assetauditDetailRepository.findAllByAuditIdOrderById(audit.getId());
    }

    Assetaudit findById(int id, String account) {
        if (id <= 0) {
            throw new RuntimeException("id不合法:" + id);
        }
        Assetaudit audit = findById(id);
        if (audit == null || audit.getState() != 0) {
            throw new RuntimeException("指定的审批不存在，或已审批:" + id);
        }
        if (!audit.getAccount().equals(account)) {
            throw new RuntimeException("指定的审批不是你发起的:" + id);
        }
        return audit;
    }
}
