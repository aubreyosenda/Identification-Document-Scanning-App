package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.SecurityPersonelDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SecurityPersonnelService {

    List<SecurityPersonelDetails> getSecurityPersonnelByBuildingId(String buildingId);

    Optional<SecurityPersonelDetails> getSecurityPersonnelByIdNumber(Long nationalIdNumber);

    Optional<SecurityPersonelDetails> getSecurityPersonnelByPhoneAndId(Long phoneNumber, Long nationalIdNumber);
}
