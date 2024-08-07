package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.CompanyDetails;
import com.vanatel.sidar.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyAPIController {

    @Autowired
    CompanyService companyService;

    @PostMapping("/register")
    public String registerCompany (@RequestBody CompanyDetails companyDetails) {
        companyService.registerCompany(companyDetails);
        return "Company registered successfully";
    }
}
