package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.BuildingDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface BuildingService {

    BuildingDetails registerBuilding(BuildingDetails buildingDetails);

    void deleteBuilding(String buildingId);

    List<BuildingDetails> getBuildingsByCompanyId(String companyId);

    Optional<Map<String, Object>> getCompanyAndBuildingNames(String buildingId);
}
