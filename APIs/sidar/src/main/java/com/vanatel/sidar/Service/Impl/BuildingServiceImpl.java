package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Service.BuildingService;
import com.vanatel.sidar.Service.Impl.IdGenerators.BuildingIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingIdGenerator buildingIdGenerator;

    @Override
    public BuildingDetails registerBuilding(BuildingDetails buildingDetails) {
        String company_id = buildingDetails.getCompanyID();
        if (company_id == null || company_id.isEmpty()) {
            throw new IllegalArgumentException("Company ID cannot be null or empty");
        }
        buildingDetails.setBuildingId(buildingIdGenerator.generateNewBuildingID());
        buildingDetails.setCompanyID(company_id);
        return buildingRepository.save(buildingDetails);
    }

    public void deleteBuilding(String buildingId) {
        buildingRepository.deleteById(buildingId);
    }

    @Override
    public List<BuildingDetails> getBuildingsByCompanyId(String companyId) {
        return buildingRepository.findByCompanyID(companyId);
    }


}
