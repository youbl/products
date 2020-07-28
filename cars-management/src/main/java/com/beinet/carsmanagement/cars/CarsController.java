package com.beinet.carsmanagement.cars;

import com.beinet.carsmanagement.cars.model.Cars;
import com.beinet.carsmanagement.cars.services.CarsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class CarsController {
    private final CarsService carsService;

    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @GetMapping("cars")
    public List<Cars> getCars() {
        return carsService.getCars();
    }
}
