package com.beinet.carsmanagement.owner;

import com.beinet.carsmanagement.owner.model.Owner;
import com.beinet.carsmanagement.owner.services.OwnerService;
import com.beinet.carsmanagement.utils.IpHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/v1")//, produces = {"application/json;charset=UTF-8"})
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }


    @GetMapping("owner")
    public List<Owner> getOwner(@RequestParam(required = false) String address) {
        return ownerService.getOwner(address);
    }

    @GetMapping("owner/{id}")
    public Owner getOwner(@PathVariable int id, @RequestParam String phone) {
        return ownerService.getOwnerBySearcherPhone(id, phone);
    }

    @PostMapping("owner")
    public Owner addOwner(HttpServletRequest request, @RequestBody Owner owner) {
        owner.setIp(IpHelper.getIpAddr(request));
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        owner.setAddTime(now);
        owner.setModifyTime(now);
        return ownerService.saveOwner(owner);
    }
}
