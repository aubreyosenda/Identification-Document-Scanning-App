package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.VisitorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorDetails, BigInteger> {
    Optional<VisitorDetails> findByVisitorDocNo(String visitorDocNo);
    List<VisitorDetails> findByVisitorFullName(String visitorName);
    List<VisitorDetails> findByVisitorPhone(long phoneNumber);
}
