package com.beinet.carsmanagement.cars.services;

import com.beinet.carsmanagement.cars.model.Cars;
import com.beinet.carsmanagement.cars.repository.CarsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CarsService {
    private final CarsRepository carsRepository;

    public CarsService(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    /**
     * 根据车牌进行模糊查询，车牌为空时返回全部
     *
     * @param carNumber 车牌
     * @return 列表（手机号被隐藏）
     */
    public List<Cars> getCars(String carNumber) {
        List<Cars> ret;
        if (StringUtils.isEmpty(carNumber)) {
            ret = carsRepository.findAll();
        } else {
            ret = carsRepository.findByCarNumberLikeOrderByAddress("%" + carNumber + "%");
        }
        for (Cars item : ret) {
            item.setPhone(hidePhone(item.getPhone()));
        }
        return ret;
    }

    /**
     * 搜索者根据自己的完整手机，查看完整车辆信息
     *
     * @param id            车辆id
     * @param searcherPhone 搜索者手机
     * @return 车辆信息
     */
    public Cars getCarBySearcherPhone(int id, String searcherPhone) {
        Cars searcher = carsRepository.findByPhone(searcherPhone);
        if (searcher == null)
            return null;
        return carsRepository.findById(id).get();
    }

    /**
     * 保存信息
     *
     * @param car 信息
     * @return 保存结果
     */
    public Cars saveCars(Cars car) {
        Cars oldData = carsRepository.findByCarNumberOrSn(car.getCarNumber(), car.getSn());
        if (oldData != null && oldData.getId() != car.getId()) {
            String msg = oldData.getSn() == car.getSn() ? "序号已被使用" : "该车牌已登记";
            throw new IllegalArgumentException(msg);
        }
        return carsRepository.save(car);
    }

    private String hidePhone(String phone) {
        if (StringUtils.isEmpty(phone))
            return "";
        return phone.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2");
    }
}
