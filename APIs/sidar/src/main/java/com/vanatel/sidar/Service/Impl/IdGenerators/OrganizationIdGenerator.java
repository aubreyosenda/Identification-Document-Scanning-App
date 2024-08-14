package com.vanatel.sidar.Service.Impl.IdGenerators;

import com.vanatel.sidar.DataBaseRepository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component


public class OrganizationIdGenerator {
    @Autowired
    OrganizationRepository organizationRepository;

    public String generateNewOrganizationId() {
        Integer lastOrganizationId = organizationRepository.findLastOrganizationId();
        if (lastOrganizationId == null) {
            int newId = lastOrganizationId + 1;
            return String.format("%05d", newId);
        } else {
            return "00001";
        }
    }
}
