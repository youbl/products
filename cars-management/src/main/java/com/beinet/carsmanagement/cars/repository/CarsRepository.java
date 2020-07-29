package com.beinet.carsmanagement.cars.repository;

import com.beinet.carsmanagement.cars.model.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarsRepository extends JpaRepository<Cars, Integer> {
    List<Cars> findByCarNumberLike(String carNumber);

    Cars findByCarNumberOrSn(String carNumber, int sn);

    Cars findByPhone(String phone);
}
