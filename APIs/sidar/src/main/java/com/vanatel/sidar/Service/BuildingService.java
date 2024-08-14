package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.BuildingDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuildingService {

    BuildingDetails registerBuilding(BuildingDetails buildingDetails);

    void deleteBuilding(String buildingId);

    List<BuildingDetails> getBuildingsByCompanyId(String companyId);
}
