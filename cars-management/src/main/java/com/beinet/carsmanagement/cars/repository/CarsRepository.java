package com.beinet.carsmanagement.cars.repository;

import com.beinet.carsmanagement.cars.model.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepository extends JpaRepository<Cars, Integer> {
}
