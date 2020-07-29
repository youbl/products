package com.beinet.carsmanagement.cars;

import com.beinet.carsmanagement.SecurityConfig;
import com.beinet.carsmanagement.cars.model.Cars;
import com.beinet.carsmanagement.cars.services.CarsService;
import com.beinet.carsmanagement.utils.IpHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/v1")//, produces = {"application/json;charset=UTF-8"})
public class CarsController {
    private final CarsService carsService;

    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @GetMapping("user")
    public String getLoginUser() {
        return SecurityConfig.getLoginUser();
    }

    @GetMapping("cars")
    public List<Cars> getCars(@RequestParam(required = false) String carNumber) {
        return carsService.getCars(carNumber);
    }

    @GetMapping("cars/{id}")
    public Cars getCars(@PathVariable int id, @RequestParam String phone) {
        return carsService.getCarBySearcherPhone(id, phone);
    }

    @PostMapping("cars")
    public Cars addCars(HttpServletRequest request, @RequestBody Cars car) {
        car.setIp(IpHelper.getIpAddr(request));
        car.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return carsService.saveCars(car);
    }
}
