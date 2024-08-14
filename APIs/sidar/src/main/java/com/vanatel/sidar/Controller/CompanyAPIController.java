package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.BuildingDetails;
import com.vanatel.sidar.Model.CompanyDetails;
import com.vanatel.sidar.Model.OrganizationDetails;
import com.vanatel.sidar.Model.dto.LoginRequest;
import com.vanatel.sidar.Service.BuildingService;
import com.vanatel.sidar.Service.CompanyService;
import com.vanatel.sidar.Service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyAPIController {

    public static final Logger logger = LoggerFactory.getLogger(CompanyAPIController.class);

    @Autowired
    CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerCompany(@Validated @RequestBody CompanyDetails companyDetails, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Optional<CompanyDetails> existingCompanyByEmail = companyService.getCompanyByEmail(companyDetails.getCompanyEmailAddress());
        if (existingCompanyByEmail.isPresent()) {
            return new ResponseEntity<>(Map.of("message", "Company with the same Email already exists!"), HttpStatus.CONFLICT);
        }

        Optional<CompanyDetails> existingCompanyByPhone = companyService.getCompanyByPhoneNumber(companyDetails.getCompanyPhoneNumber());
        if (existingCompanyByPhone.isPresent()) {
            return new ResponseEntity<>(Map.of("message", "Company with the same Phone number already exists!"), HttpStatus.CONFLICT);
        }

        companyService.registerCompany(companyDetails);

        return new ResponseEntity<>(Map.of("message", "Company registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginCompany(@Validated @RequestBody LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
            return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
        }

        boolean isValid = companyService.validateCompanyCredentials(loginRequest.getCompanyEmailAddress(), loginRequest.getCompanyPassword());
        if (!isValid) {
            return new ResponseEntity<>(Map.of("message", "Login failed"), HttpStatus.UNAUTHORIZED);
        } else {
            Optional<CompanyDetails> companyDetails = companyService.getCompanyByEmail(loginRequest.getCompanyEmailAddress());
            if (companyDetails.isPresent()) {
                CompanyDetails details = companyDetails.get();
                Map<String, Object> profile = new HashMap<>();
                profile.put("companyName", details.getCompanyName());
                profile.put("companyId", details.getCompanyId());
                // Add more company profile details here if needed

                return new ResponseEntity<>(Map.of("message", "Login successful", "profile", profile), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("message", "Company details not found"), HttpStatus.NOT_FOUND);
            }
        }
    }

    /********************************************************************
     *                      BUILDING ENDPOINTS
     * ******************************************************************/

    @Autowired
    BuildingService buildingService;

    @PostMapping("/buildings/register")
    public ResponseEntity<Map<String, String>> registerBuilding(@Validated @RequestBody BuildingDetails buildingDetails) {

        if (buildingDetails.getCompanyID() == null || buildingDetails.getCompanyID().isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "Company ID is null or empty"), HttpStatus.BAD_REQUEST);
        }

        BuildingDetails registeredBuilding = buildingService.registerBuilding(buildingDetails);
        logger.info("Building {} for company ID {} registered successfully", registeredBuilding.getBuildingName(), registeredBuilding.getCompanyID());
        return new ResponseEntity<>(Map.of("message", "Building registered Successfully", "buildingId", registeredBuilding.getBuildingId()), HttpStatus.OK);
    }

    @GetMapping("/buildings/list")
    public ResponseEntity<List<BuildingDetails>> getBuildingsByCompanyID(@RequestParam("companyID") String companyID) {
        List<BuildingDetails> buildings = buildingService.getBuildingsByCompanyId(companyID);
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }


    /********************************************************************
     *                      ORGANIZATIONS ENDPOINTS
     * ******************************************************************/

    @Autowired
    OrganizationService organizationService;

    @PostMapping("/buildings/organizations/register")
    public ResponseEntity<Map<String, String>> addOrganization(@Validated @RequestBody OrganizationDetails organizationDetails) {
        logger.info("Received organization details: {}", organizationDetails);

        if (organizationDetails.getBuildingId() == null || organizationDetails.getBuildingId().isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "Building ID is null or empty"), HttpStatus.BAD_REQUEST);
        }
        organizationService.addOrganization(organizationDetails);
        logger.info("Building ID {} for organization {} registered successfully", organizationDetails.getBuildingId(), organizationDetails.getOrganizationName());
        return new ResponseEntity<>(Map.of("message", "Organization registered successfully"), HttpStatus.OK);
    }

    @GetMapping("/building/organizations/list")
    public ResponseEntity<List<OrganizationDetails>> getOrganizationsByBuildingID(@RequestParam("BuildingID") String buildingID) {
        List<OrganizationDetails> organizations = organizationService.getOrganizationsByBuildingId(buildingID);
        return new ResponseEntity<>(organizations, HttpStatus.OK);
    }

    @GetMapping("/building/organizations/byFloor")
    public ResponseEntity<List<OrganizationDetails>> getOrganizationsByFloor(@RequestParam("BuildingID") String buildingID, @RequestParam("floorNumber") int floorNumber) {

        List<OrganizationDetails> organizationsByFloors = organizationService.findByBuildingIdAndFloorNumber(buildingID, floorNumber);
        return new ResponseEntity<>(organizationsByFloors, HttpStatus.OK);
    }

}
