package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.BuildingDetails;

import java.util.List;

public interface BuildingService {
    BuildingDetails registerBuilding(BuildingDetails buildingDetails);

    void deleteBuilding(String buildingId);

    List<BuildingDetails> getBuildingsByCompanyId(String companyId);
}
