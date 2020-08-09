package com.beinet.carsmanagement.carsTemp.cars.repository;

import com.beinet.carsmanagement.carsTemp.cars.model.CarsTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarsTempRepository extends JpaRepository<CarsTemp, Integer> {
    List<CarsTemp> findByCarNumberLikeOrderByCarNumber(String carNumber);

    List<CarsTemp> findByOrderByCarNumber();
}
