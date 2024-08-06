package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.CompanyRepository;
import com.vanatel.sidar.Model.CompanyDetails;
import com.vanatel.sidar.Service.CompanyService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

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

    @Override
    public boolean validateCompanyCredentials(String companyEmailAddress, String companyPassword) {
        CompanyDetails companyDetails = companyRepository.findByCompanyEmailAddress(companyEmailAddress);
        return companyDetails != null && companyDetails.getCompanyPassword().equals(companyPassword);
    }

    @Override
    public Optional<CompanyDetails> getCompanyByEmail(String companyEmailAddress) {
        return Optional.ofNullable(companyRepository.findByCompanyEmailAddress(companyEmailAddress));
    }

    @Override
    public Optional<CompanyDetails> getCompanyByPhoneNumber(Long phoneNumber) {
        return companyRepository.findByCompanyPhoneNumber(phoneNumber);
    }
}
