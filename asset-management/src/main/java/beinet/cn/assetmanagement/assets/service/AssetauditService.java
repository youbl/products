package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.model.AssetauditDetail;
import beinet.cn.assetmanagement.assets.model.AssetauditDto;
import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.repository.AssetauditDetailRepository;
import beinet.cn.assetmanagement.assets.repository.AssetauditRepository;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Assetaudit> findAuditByType(String account, String type) {
        if (StringUtils.isEmpty(account) || account.equals("匿名")) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(type)) {
            return new ArrayList<>();
        }
        List<String> arrType = new ArrayList<>();
        for (String item : type.split(",")) {
            if (item.length() > 0) {
                arrType.add(item);
            }
        }
        if (arrType.isEmpty()) {
            return new ArrayList<>();
        }
        return assetauditRepository.findAllByAccountAndTypeInOrderByIdDesc(account, arrType);
    }

    public Assetaudit findById(Integer id) {
        return assetauditRepository.findById(id).orElse(null);
    }

    @Transactional
    public void newAudit(AssetauditDto dto) {
        if (dto.getAssetCodes() == null || dto.getAssetCodes().length <= 0) {
            throw new RuntimeException("退库资产列表为空");
        }
        for (String code : dto.getAssetCodes()) {
            Assets assets = assetsService.findByCode(code);
            if (assets == null) {
                throw new RuntimeException("退库资产code不存在：" + code);
            }
            Assetaudit item = dto.mapTo();
            item.setState(0);
            item.setId(0);
            item.setClassId(assets.getClassId());
            if (item.getType().equals("assetReturn")) {
                item.setReturnTime(LocalDateTime.now());
            }
            save(item);

            AssetauditDetail detail = AssetauditDetail.builder()
                    .auditId(item.getId())
                    .code(code)
                    .build();
            assetauditDetailRepository.save(detail);
        }
    }

    public Assetaudit save(Assetaudit item) {
        if (item == null) {
            return null;
        }
        return assetauditRepository.save(item);
    }

    public void cancelAudit(int id, String account) {
        Assetaudit audit = findById(id, account, true);
        audit.setState(2);
        save(audit);
    }

    @Transactional
    public void doAudit(AssetauditDto item) {
        if (item.getState() != 1 && item.getState() != 8) {
            throw new RuntimeException("审核状态设置有误:" + item.getState());
        }
        if (item.getState() == 8 && (item.getAssetCodes() == null || item.getAssetCodes().length == 0)) {
            throw new RuntimeException("审核通过时，必须分配资产");
        }
        Assetaudit audit = findById(item.getId(), item.getAccount(), true);
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
        Assetaudit audit = findById(auditId, account, false);
        List<AssetauditDetail> result = assetauditDetailRepository.findAllByAuditIdOrderById(audit.getId());
        fillAssetName(result);
        return result;
    }

    private void fillAssetName(List<AssetauditDetail> result) {
        if (result == null || result.isEmpty()) {
            return;
        }
        List<String> codeArr = result.stream().map(AssetauditDetail::getCode).collect(Collectors.toList());
        for (Assets assets : assetsService.findByCodeArr(codeArr)) {
            for (AssetauditDetail detail : result) {
                if (detail.getCode().equals(assets.getCode())) {
                    detail.setAssetName(assets.getAssetName());
                    break;
                }
            }
        }
    }

    Assetaudit findById(int id, String account, boolean mustNotAudit) {
        if (id <= 0) {
            throw new RuntimeException("id不合法:" + id);
        }
        Assetaudit audit = findById(id);
        if (audit == null || (mustNotAudit && audit.getState() != 0)) {
            throw new RuntimeException("指定的审批不存在，或已审批:" + id);
        }
        if (!audit.getAccount().equals(account)) {
            throw new RuntimeException("指定的审批不是你发起的:" + id);
        }
        return audit;
    }
}
