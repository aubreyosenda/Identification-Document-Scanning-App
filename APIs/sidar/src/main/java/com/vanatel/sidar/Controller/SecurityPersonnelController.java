package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.SecurityPersonelDetails;
import com.vanatel.sidar.Model.dto.SecurityPersonnelDetailsDTO;
import com.vanatel.sidar.Service.SecurityPersonnelService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    private static final Logger log = LoggerFactory.getLogger(SecurityPersonnelController.class);
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
            @RequestParam("nationalIdNumber") Long nationalIdNumber,
            HttpServletRequest request) {

        logger.info("Login attempt for phoneNumber: {}, nationalIdNumber: {}", phoneNumber, nationalIdNumber);

        Optional<SecurityPersonelDetails> optionalDetails = securityPersonnelService.getSecurityPersonnelByPhoneAndId(phoneNumber, nationalIdNumber);

        if (optionalDetails.isEmpty()) {
            logger.warn("No security personnel found with phoneNumber: {} and nationalIdNumber: {}", phoneNumber, nationalIdNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        SecurityPersonelDetails details = optionalDetails.get();

        // Make the call to the Building & Company endpoint
        String buildingId = details.getBuildingId();

        String buildingCompanyUrl = ServletUriComponentsBuilder.fromContextPath(request)
//                .scheme("https")
                .path("/api/v1/company/Building&Company-Name")
                .queryParam("buildingId", buildingId)
                .toUriString();
        logger.info(buildingCompanyUrl);

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

            // Log the raw response for debugging
            String rawResponse = responseBuilder.toString();
            logger.info("Raw response from Building & Company endpoint: {}", rawResponse);

            // Parse the Building & Company response
            if (rawResponse.startsWith("{") && rawResponse.endsWith("}")) {

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
            } else{
                logger.error("Failed to parse response from Building & Company endpoint: \n{}", rawResponse);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            logger.error("Error during login process", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
