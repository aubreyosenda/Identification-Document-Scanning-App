package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import com.vanatel.sidar.DataBaseRepository.SecurityPersonnelRepository;
import com.vanatel.sidar.Model.SecurityPersonelDetails;
import com.vanatel.sidar.Service.SecurityPersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityPersonnelServiceImpl implements SecurityPersonnelService {
    @Autowired
    private SecurityPersonnelRepository securityPersonnelRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<SecurityPersonelDetails> getSecurityPersonnelByBuildingId(String buildingId) {
        return securityPersonnelRepository.findByBuildingId(buildingId);
    }

    @Override
    public Optional<SecurityPersonelDetails> getSecurityPersonnelByIdNumber(Long nationalIdNumber) {
        return securityPersonnelRepository.findByNationalIdNumber(nationalIdNumber);
    }

    // Find security personnel by phone number and National ID
    @Override
    public Optional<SecurityPersonelDetails> getSecurityPersonnelByPhoneAndId(Long phoneNumber, Long nationalIdNumber) {
        return securityPersonnelRepository.findByPhoneNumberAndNationalIdNumber(phoneNumber, nationalIdNumber);
    }
}
