package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingIdGenerator buildingIdGenerator;

    public BuildingDetails registerBuilding(BuildingDetails buildingDetails) {
        String newBuildingId = BuildingIdGenerator.generateNewBuildingID();
        buildingDetails.setBuildingId(newBuildingId);
        return buildingRepository.save(buildingDetails);
    }

    public void deleteBuilding(String buildingId) {
        buildingRepository.deleteById(buildingId);
    }


    public List<BuildingDetails> getBuildingsByCompanyId(String companyId) {
        return null;
    }
}
