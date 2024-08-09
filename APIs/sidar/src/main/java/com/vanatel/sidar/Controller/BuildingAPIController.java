package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

        import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingAPIController {

    @Autowired
    private BuildingService buildingService;

//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> registerBuilding(@RequestBody BuildingDetails buildingDetails) {
//        String loggedInCompanyId = getLoggedInCompanyId();
//        if(!buildingDetails.getCompanyID().equals(loggedInCompanyId)) {
//            return new ResponseEntity<>(Map.of("message", "Invalid company ID"), HttpStatus.BAD_REQUEST);
//        }
//        BuildingDetails registeredBuilding = buildingService.registerBuilding(buildingDetails);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Building registered successfully");
//        response.put("buildingId", registeredBuilding.getBuildingId());
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{buildingId}")
//    public ResponseEntity<Map<String, String>> deleteBuilding(@PathVariable String buildingId) {
//        buildingService.deleteBuilding(buildingId);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Building deleted successfully");
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerBuilding(@RequestBody BuildingDetails buildingDetails) {
        String companyId = getLoggedInCompanyId(); // Assuming you have a method to retrieve the authenticated company ID
        buildingDetails.setCompanyID(companyId);

        buildingService.registerBuilding(buildingDetails);
        return new ResponseEntity<>(Map.of("message", "Building registered successfully", "buildingId", buildingDetails.getBuildingId()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BuildingDetails>> getBuildings() {
        String companyId = getLoggedInCompanyId();
        List<BuildingDetails> buildings = buildingService.getBuildingsByCompanyId(companyId);
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<Map<String, String>> deleteBuilding(@PathVariable String buildingId) {
        buildingService.deleteBuilding(buildingId);
        return new ResponseEntity<>(Map.of("message", "Building deleted successfully"), HttpStatus.OK);
    }

    private String getLoggedInCompanyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&  authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // or retrieve company ID if stored differently
        }

        return null;
    }

    // Other endpoints related to buildings
}
