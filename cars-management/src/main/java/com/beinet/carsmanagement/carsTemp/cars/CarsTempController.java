package com.beinet.carsmanagement.carsTemp.cars;

import com.beinet.carsmanagement.SecurityConfig;
import com.beinet.carsmanagement.cars.model.Cars;
import com.beinet.carsmanagement.carsTemp.cars.model.CarsTemp;
import com.beinet.carsmanagement.carsTemp.cars.services.CarsTempService;
import com.beinet.carsmanagement.utils.IpHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/v1")//, produces = {"application/json;charset=UTF-8"})
public class CarsTempController {
    private final CarsTempService carsService;

    public CarsTempController(CarsTempService carsService) {
        this.carsService = carsService;
    }

    @GetMapping("carstemp")
    public List<CarsTemp> getCars(@RequestParam(required = false) String carNumber) {
        return carsService.getCars(carNumber);
    }


    @PostMapping("carstemp")
    public CarsTemp addCars(HttpServletRequest request, @RequestBody CarsTemp car) {
        car.setIp(IpHelper.getIpAddr(request));
        car.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return carsService.saveCars(car);
    }
}
