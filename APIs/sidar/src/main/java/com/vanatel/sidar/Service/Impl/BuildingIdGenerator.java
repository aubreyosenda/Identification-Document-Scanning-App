package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class BuildingIdGenerator {

    @Autowired
    static BuildingRepository buildingRepository;

    public static String generateNewBuildingID() {
        String lastBuildingID = buildingRepository.findLastBuildingID();
        if (lastBuildingID != null) {
            int lastIdNumber = Integer.parseInt(lastBuildingID.substring(3)); // Extract the number part after "BID"
            int newIdNumber = lastIdNumber + 1;
            return String.format("BID%06d", newIdNumber); // Format the ID as BID followed by a 4-digit number
        } else {
            return "BID0001"; // Default if no IDs are present in the database
        }
    }
}
