package com.vanatel.sidar.Service.Impl.IdGenerators;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.vanatel.sidar.Controller.CompanyAPIController.logger;

@Service
@Component
public class BuildingIdGenerator {

    @Autowired
    private BuildingRepository buildingRepository;

    public String generateNewBuildingID() {
        String lastBuildingID = buildingRepository.findLastBuildingID();
        if (lastBuildingID != null) {
            int lastIdNumber = Integer.parseInt(lastBuildingID.substring(3)); // Extract the number part after "BID"
            int newIdNumber = lastIdNumber + 1;
            logger.info("Generated Building id: {}", newIdNumber);
            return String.format("BID%04d", newIdNumber); // Format the ID as BID followed by a 6-digit number
        } else {
            return "BID0001"; // Default, if no IDs are present in the database
        }
    }
}
