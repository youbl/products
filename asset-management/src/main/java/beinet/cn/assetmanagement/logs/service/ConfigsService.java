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
        return configsRepository.findByTypeOrderById(type);
    }

    public Configs findById(Integer id) {
        return configsRepository.findById(id).orElse(null);
    }

    public Configs save(Configs item) {
        if (item == null) {
            return null;
        }
        return configsRepository.save(item);
    }

}
