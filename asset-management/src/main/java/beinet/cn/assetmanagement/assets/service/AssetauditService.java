package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetaudit;
import beinet.cn.assetmanagement.assets.repository.AssetauditRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetauditService {
    private final AssetauditRepository assetauditRepository;

    public AssetauditService(AssetauditRepository assetauditRepository) {
        this.assetauditRepository = assetauditRepository;
    }

    public List<Assetaudit> findAll() {
        return assetauditRepository.findAll();
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

}
