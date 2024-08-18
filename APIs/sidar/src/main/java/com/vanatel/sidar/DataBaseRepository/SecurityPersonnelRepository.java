package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.SecurityPersonelDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityPersonnelRepository extends JpaRepository<SecurityPersonelDetails, Integer> {
    List<SecurityPersonelDetails> findByBuildingId(String buildingId);

    Optional<SecurityPersonelDetails> findByNationalIdNumber(Long nationalIdNumber);

    // Find by phone number and National ID
    Optional<SecurityPersonelDetails> findByPhoneNumberAndNationalIdNumber(Long phoneNumber, Long nationalIdNumber);
}


