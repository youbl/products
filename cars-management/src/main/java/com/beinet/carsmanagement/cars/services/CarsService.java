package com.beinet.carsmanagement.cars.services;

import com.beinet.carsmanagement.cars.model.Cars;
import com.beinet.carsmanagement.cars.repository.CarsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarsService {
    private final CarsRepository carsRepository;

    public CarsService(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    public List<Cars> getCars() {
        return carsRepository.findAll();
    }
}
