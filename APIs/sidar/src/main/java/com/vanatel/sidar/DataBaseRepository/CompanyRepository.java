package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDetails, String> {

}
