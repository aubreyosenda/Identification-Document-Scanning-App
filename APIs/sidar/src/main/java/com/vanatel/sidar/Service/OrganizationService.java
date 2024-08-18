package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.OrganizationDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrganizationService {
    ResponseEntity<Map<String, String>> addOrganization(OrganizationDetails organizationDetails);
    OrganizationDetails updateOrganization(Integer organizationId);
    List<OrganizationDetails> getOrganizationsByBuildingId(String buildingId);

    List<OrganizationDetails> findByBuildingIdAndFloorNumber(String buildingID, int floorNumber);
}
