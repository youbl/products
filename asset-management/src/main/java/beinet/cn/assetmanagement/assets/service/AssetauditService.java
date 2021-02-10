package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.repository.AssetauditRepository;
import beinet.cn.assetmanagement.security.AuthDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetauditService {
    private final AssetauditRepository assetauditRepository;

    public AssetauditService(AssetauditRepository assetauditRepository) {
        this.assetauditRepository = assetauditRepository;
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
        if (id <= 0) {
            return;
        }
        Assetaudit audit = findById(id);
        if (audit == null || audit.getState() != 0) {
            throw new RuntimeException("指定的审批不存在，或已审批:" + id);
        }
        if (!audit.getAccount().equals(account)) {
            throw new RuntimeException("指定的审批不是你发起的:" + id);
        }
        audit.setState(2);
        save(audit);

    }
}
