package beinet.cn.assetmanagement.assets.service;

import beinet.cn.assetmanagement.assets.model.Assets;
import beinet.cn.assetmanagement.assets.repository.AssetsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AssetsService {
    private final AssetsRepository assetsRepository;
    private final AssetclassService assetclassService;

    public AssetsService(AssetsRepository assetsRepository,
                         AssetclassService assetclassService) {
        this.assetsRepository = assetsRepository;
        this.assetclassService = assetclassService;
    }

    public List<Assets> findAll() {
        return assetsRepository.findAll();
    }

    public Assets findById(Integer id) {
        return assetsRepository.findById(id).orElse(null);
    }

    public Assets save(Assets item) {
        if (item == null) {
            return null;
        }
        if (StringUtils.isEmpty(item.getCode())) {
            item.setCode(getCode(item));
        }
        return assetsRepository.save(item);
    }


    /**
     * 8位年月日 + 4位分类 + 6位序号
     *
     * @return
     */
    public String getCode(Assets item) {
        String code = item.getBuyTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                String.format("%04d", item.getClassId()) +
                String.format("%06d", assetclassService.updateAndGetAmount(item.getClassId()));

        return code;
    }
}
