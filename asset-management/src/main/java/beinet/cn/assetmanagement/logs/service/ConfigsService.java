package beinet.cn.assetmanagement.logs.service;

import beinet.cn.assetmanagement.logs.model.Configs;
import beinet.cn.assetmanagement.logs.repository.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigsService {
    private final ConfigsRepository configsRepository;

    public ConfigsService(ConfigsRepository configsRepository) {
        this.configsRepository = configsRepository;
    }

    public List<Configs> findAll() {
        return configsRepository.findAll();
    }

    public List<Configs> findAll(String type) {
        if (StringUtils.isEmpty(type)) {
            return new ArrayList<>();
        }
        List<String> arrType = new ArrayList<>();
        for (String item : type.split(",")) {
            if (item.length() > 0) {
                arrType.add(item);
            }
        }
        return configsRepository.findByTypeInOrderByTypeAscCodeAsc(arrType);
    }

    public Configs findById(Integer id) {
        return configsRepository.findById(id).orElse(null);
    }

    public Configs save(Configs item) {
        if (item == null) {
            return null;
        }
        Configs configs = configsRepository.findByTypeAndCode(item.getType(), item.getCode());
        if (configs != null && configs.getId() != item.getId()) {
            throw new RuntimeException("此类型+键值已存在，不允许重复");
        }
        return configsRepository.save(item);
    }

}
