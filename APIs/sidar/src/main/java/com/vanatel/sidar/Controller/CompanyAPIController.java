package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.CompanyDetails;
import com.vanatel.sidar.Model.dto.LoginRequest;
import com.vanatel.sidar.Service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyAPIController {

    @Autowired
    CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerCompany(@Valid @RequestBody CompanyDetails companyDetails, BindingResult result) {
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
                // Add more company profile details here if needed

                return new ResponseEntity<>(Map.of("message", "Login successful", "profile", profile), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("message", "Company details not found"), HttpStatus.NOT_FOUND);
            }
        }
    }

}
