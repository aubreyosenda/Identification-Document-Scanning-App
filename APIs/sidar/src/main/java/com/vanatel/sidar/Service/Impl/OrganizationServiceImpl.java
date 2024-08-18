package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import com.vanatel.sidar.DataBaseRepository.OrganizationRepository;
import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Model.OrganizationDetails;
import com.vanatel.sidar.Service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizationServiceImpl implements OrganizationService {
//    public static final logger logger = new Logger(OrganizationServiceImpl.class);

    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    BuildingDetails buildingDetails;

    @Override
    public ResponseEntity<Map<String, String>> addOrganization(OrganizationDetails organizationDetails) {
        String buildingId = organizationDetails.getBuildingId();
        if (buildingId == null || buildingId.isEmpty()) {
            throw new IllegalArgumentException("Building ID cannot be null or empty");
        } else if (!buildingRepository.existsById(organizationDetails.getBuildingId())) {
            throw new IllegalArgumentException("Building ID :" + buildingId + " does not exist");
        }
//        else if (organizationDetails.getFloorNumber() < -1 || organizationDetails.getFloorNumber() > buildingDetails.getNumberOfFloors()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid floor number for Building: "+ buildingDetails.getBuildingName()));

//        }
        organizationDetails.setBuildingId(buildingId);
        organizationDetails.setOrganizationId(organizationRepository.findLastOrganizationId());
        organizationRepository.save(organizationDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Organization registered successfully"));
    }

    @Override
    public OrganizationDetails updateOrganization(Integer organizationId) {
        return null;
    }

    @Override
    public List<OrganizationDetails> getOrganizationsByBuildingId(String buildingId) {
        return organizationRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<OrganizationDetails> findByBuildingIdAndFloorNumber(String buildingID, int floorNumber) {
        return organizationRepository.findByBuildingIdAndFloorNumber(buildingID, floorNumber);
    }

}
