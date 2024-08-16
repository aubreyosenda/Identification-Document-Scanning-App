package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.SecurityPersonelDetails;
import com.vanatel.sidar.Model.dto.SecurityPersonnelDetailsDTO;
import com.vanatel.sidar.Service.SecurityPersonnelService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static com.vanatel.sidar.Controller.CompanyAPIController.logger;

@RestController
@RequestMapping("/api/v1/company/")
public class SecurityPersonnelController {

    @Autowired
    private SecurityPersonnelService securityPersonnelService;

//    Security personnel for each building
    @GetMapping("/building/securityPersonnel")
    public List<SecurityPersonelDetails> getSecurityPersonnelByBuildingId(@RequestParam("BuildingID") String buildingId) {
        return securityPersonnelService.getSecurityPersonnelByBuildingId(buildingId);
    }

//    Get personnel Details based on an ID number
    @GetMapping("/security-personnel/byNationalIdNumber")
    public Optional<SecurityPersonelDetails> getSecurityPersonnelByIdNumber(@RequestParam("nationalIdNumber") Long nationalIdNumber) {
        return securityPersonnelService.getSecurityPersonnelByIdNumber(nationalIdNumber);
    }

    @GetMapping("/personnel/login")
    public ResponseEntity<SecurityPersonnelDetailsDTO> loginSecurityPersonnel(
            @RequestParam("phoneNumber") Long phoneNumber,
            @RequestParam("nationalIdNumber") Long nationalIdNumber) {

        logger.info("Login attempt for phoneNumber: {}, nationalIdNumber: {}", phoneNumber, nationalIdNumber);

        Optional<SecurityPersonelDetails> optionalDetails = securityPersonnelService.getSecurityPersonnelByPhoneAndId(phoneNumber, nationalIdNumber);

        if (optionalDetails.isEmpty()) {
            logger.warn("No security personnel found with phoneNumber: {} and nationalIdNumber: {}", phoneNumber, nationalIdNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        SecurityPersonelDetails details = optionalDetails.get();

        // Make the call to the Building & Company endpoint
        String buildingId = details.getBuildingId();
        String buildingCompanyUrl = "http://localhost:5500/api/v1/company/Building&Company-Name?buildingId=" + buildingId;

        try {
            URL url = new URL(buildingCompanyUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Parse the Building & Company response
            JSONObject buildingCompanyResponse = new JSONObject(responseBuilder.toString());
            String buildingName = buildingCompanyResponse.getString("buildingName");
            String companyId = buildingCompanyResponse.getString("companyId");
            String companyName = buildingCompanyResponse.getString("companyName");


            // Map entity to DTO and enrich with additional details
            SecurityPersonnelDetailsDTO dto = new SecurityPersonnelDetailsDTO(
                    details.getId(),
                    details.getFullName(),
                    details.getNationalIdNumber(),
                    details.getWorkIdNumber(),
                    details.getPhoneNumber(),
                    details.getBuildingId(),
                    buildingName,
                    companyId,
                    companyName
            );
            logger.info("Security personnel login successful");
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            logger.error("Error during login process", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
