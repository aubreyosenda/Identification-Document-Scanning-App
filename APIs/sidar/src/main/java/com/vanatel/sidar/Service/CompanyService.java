package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.CompanyDetails;

import java.util.Optional;

public interface CompanyService {
    String registerCompany(CompanyDetails companyDetails);
    boolean validateCompanyCredentials(String companyEmailAddress, String companyPassword);
    Optional<CompanyDetails> getCompanyByEmail(String companyEmailAddress);

    Optional<CompanyDetails> getCompanyByPhoneNumber(Long phoneNumber);
}
