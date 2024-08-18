package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Service.BuildingService;
import com.vanatel.sidar.Service.Impl.IdGenerators.BuildingIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {
    private static final Logger logger = LoggerFactory.getLogger(BuildingServiceImpl.class);
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


    public Optional<Map<String, Object>> getCompanyAndBuildingNames(String buildingId) {
        logger.debug("Entering getCompanyAndBuildingNames with buildingId: {}", buildingId);

        Optional<Object[]> queryResult = buildingRepository.findCompanyAndBuildingNamesByBuildingId(buildingId);

        if (queryResult.isEmpty()) {
            logger.debug("Query returned no results for buildingId: {}", buildingId);
            return Optional.empty();
        }

        return queryResult.map(result -> {
            logger.debug("Query returned result of size: {}", result.length);
            for (int i = 0; i < result.length; i++) {
                logger.debug("Result[{}]: {}", i, result[i]);
            }

            Map<String, Object> response = new HashMap<>();

            // Ensure we are handling the object within the result array
            if (result.length == 1 && result[0] instanceof Object[]) {
                Object[] innerResult = (Object[]) result[0];
                logger.debug("Inner result size: {}", innerResult.length);
                if (innerResult.length >= 3) {
                    response.put("buildingName", innerResult[0]);
                    response.put("companyId", innerResult[1]);
                    response.put("companyName", innerResult[2]);
                    logger.debug("Successfully extracted data: {}", response);
                } else {
                    logger.error("Unexpected inner result size for buildingId: {}", buildingId);
                    throw new RuntimeException("Unexpected inner result size");
                }
            } else {
                logger.error("Unexpected result format for buildingId: {}", buildingId);
                throw new RuntimeException("Unexpected result format");
            }
            return response;
        });
    }

}
