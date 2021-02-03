package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assetclass;
import beinet.cn.assetmanagement.assets.repository.AssetclassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetclassService {
    private final AssetclassRepository assetclassRepository;

    public AssetclassService(AssetclassRepository assetclassRepository) {
        this.assetclassRepository = assetclassRepository;
    }

    public List<Assetclass> findAll() {
        return assetclassRepository.findAll();
    }

    public Assetclass findById(Integer id) {
        return assetclassRepository.findById(id).orElse(null);
    }

    public Assetclass save(Assetclass item) {
        if (item == null) {
            return null;
        }
        return assetclassRepository.save(item);
    }

    @Transactional
    public int updateAndGetAmount(int classId) {
        Assetclass assetclass = findById(classId);
        if (assetclass == null) {
            throw new RuntimeException("指定的分类不存在:" + classId);
        }
        assetclass.setTotalAmount(assetclass.getTotalAmount() + 1);
        save(assetclass);
        return assetclass.getTotalAmount();
    }
}
