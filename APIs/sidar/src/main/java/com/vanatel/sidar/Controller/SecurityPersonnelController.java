package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.SecurityPersonelDetails;
import com.vanatel.sidar.Service.SecurityPersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/company/")
public class SecurityPersonnelController {

    @Autowired
    private SecurityPersonnelService securityPersonnelService;

//    Security personnel for each building
    @GetMapping("/building/securityPersonnel")
    public List<SecurityPersonelDetails> getSecurityPersonnelByBuildingId(@RequestParam("BuildingID") String buildingId) {
        return securityPersonnelService.getSecurityPersonnelByBuildingId(buildingId);
    }

//    Get personnel Details based on an ID number
    @GetMapping("/security-personnel/byNationalIdNumber")
    public Optional<SecurityPersonelDetails> getSecurityPersonnelByIdNumber(@RequestParam("nationalIdNumber") Long nationalIdNumber) {
        return securityPersonnelService.getSecurityPersonnelByIdNumber(nationalIdNumber);
    }

    // Login endpoint for security personnel
    @GetMapping("/personnel/login")
    public Optional<SecurityPersonelDetails> loginSecurityPersonnel(
            @RequestParam("phoneNumber") Long phoneNumber,
            @RequestParam("nationalIdNumber") Long nationalIdNumber) {
        return securityPersonnelService.getSecurityPersonnelByPhoneAndId(phoneNumber, nationalIdNumber);
    }
}
