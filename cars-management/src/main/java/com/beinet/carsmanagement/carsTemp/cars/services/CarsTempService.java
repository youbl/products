package com.beinet.carsmanagement.carsTemp.cars.services;

import com.beinet.carsmanagement.carsTemp.cars.model.CarsTemp;
import com.beinet.carsmanagement.carsTemp.cars.repository.CarsTempRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CarsTempService {
    private final CarsTempRepository carsRepository;

    public CarsTempService(CarsTempRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    /**
     * 根据车牌进行模糊查询，车牌为空时返回全部
     *
     * @param carNumber 车牌
     * @return 列表（手机号被隐藏）
     */
    public List<CarsTemp> getCars(String carNumber) {
        List<CarsTemp> ret;
        if (StringUtils.isEmpty(carNumber)) {
            ret = carsRepository.findByOrderByCarNumber();
        } else {
            ret = carsRepository.findByCarNumberLikeOrderByCarNumber("%" + carNumber + "%");
        }
        return ret;
    }


    /**
     * 保存信息
     *
     * @param car 信息
     * @return 保存结果
     */
    public CarsTemp saveCars(CarsTemp car) {
        return carsRepository.save(car);
    }

}
