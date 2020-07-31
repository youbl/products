package com.beinet.carsmanagement.owner.services;

import com.beinet.carsmanagement.SecurityConfig;
import com.beinet.carsmanagement.owner.model.Owner;
import com.beinet.carsmanagement.owner.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * 根据住址进行模糊查询，为空时返回全部
     *
     * @param address 住址
     * @return 列表（手机号被隐藏）
     */
    public List<Owner> getOwner(String address) {
        List<Owner> ret;
        if (StringUtils.isEmpty(address)) {
            ret = ownerRepository.findByOrderByAddress();
        } else {
            ret = ownerRepository.findByAddressLikeOrderByAddress("%" + address + "%");
        }
        if (!SecurityConfig.isLogin()) {
            // 未登录时，隐藏部分手机号
            for (Owner item : ret) {
                item.setPhone(hidePhone(item.getPhone()));
            }
        }
        return ret;
    }

    /**
     * 搜索者根据自己的完整手机，查看完整业主信息
     *
     * @param id            业主id
     * @param searcherPhone 搜索者手机
     * @return 业主信息
     */
    public Owner getOwnerBySearcherPhone(int id, String searcherPhone) {
        Owner searcher = ownerRepository.findByPhone(searcherPhone);
        if (searcher == null)
            return null;
        return ownerRepository.findById(id).get();
    }

    /**
     * 保存信息
     *
     * @param owner 信息
     * @return 保存结果
     */
    public Owner saveOwner(Owner owner) {
        Owner oldData = ownerRepository.findByPhone(owner.getPhone());
        if (oldData != null && oldData.getId() != owner.getId()) {
            throw new IllegalArgumentException("该手机号已被登记:" + owner.getPhone());
        }
        oldData = ownerRepository.findByAddress(owner.getAddress());
        if (oldData != null && oldData.getId() != owner.getId()) {
            throw new IllegalArgumentException("该地址已登记过:" + owner.getAddress());
        }
        return ownerRepository.save(owner);
    }

    private String hidePhone(String phone) {
        if (StringUtils.isEmpty(phone))
            return "";
        return phone.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2");
    }
}
