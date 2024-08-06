package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.CompanyRepository;
import com.vanatel.sidar.Model.CompanyDetails;
import com.vanatel.sidar.Service.CompanyService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;

    }
    @Override
    public String registerCompany(CompanyDetails companyDetails) {
//        Set Company ID
        companyDetails.setCompanyId(CompanyIdGenerator.generateCompanyId());
        companyDetails.setCreationDate(new Timestamp(System.currentTimeMillis()));
        companyRepository.save(companyDetails);
        return "Company Registered Successfully";
    }
}
