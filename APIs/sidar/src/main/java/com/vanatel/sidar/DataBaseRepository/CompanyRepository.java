package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDetails, String> {
    CompanyDetails findByCompanyEmailAddress(String companyEmailAddress);
    Optional<CompanyDetails> findByCompanyPhoneNumber(Long phoneNumber);
//    Optional<CompanyDetails> findByCompanyEmailAddress(String email);
}
