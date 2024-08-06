package com.vanatel.sidar.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Entity
@Table (name = "Company_Details")

public class CompanyDetails {

    @Id
    @Column(name = "company_id")
    private String companyId;

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 225, message = "Company Name must be more than 5 characters")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "Company email is required")
    @Email(message = "Email should be valid")
    @Column(name = "company_email", nullable = false)
    private String companyEmailAddress;

    @NotNull(message = "Phone number is required")
    @Column(name = "phone_number", nullable = false)
    private Long companyPhoneNumber;

    @NotBlank(message = "Company HQ location is required")
    @Column(name = "company_hq_location", nullable = false)
    private String companyPhysicalAddress;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Timestamp creationDate;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password Must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String companyPassword;

    public CompanyDetails() {
    }

    public CompanyDetails(String companyId, String companyName, String companyEmailAddress,
                          long companyPhoneNumber, String companyPhysicalAddress,
                          Timestamp creationDate, String companyPassword) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.companyEmailAddress = companyEmailAddress;
        this.companyPhoneNumber = companyPhoneNumber;
        this.companyPhysicalAddress = companyPhysicalAddress;
        this.creationDate = creationDate;
        this.companyPassword = companyPassword;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmailAddress() {
        return companyEmailAddress;
    }

    public void setCompanyEmailAddress(String companyEmailAddress) {
        this.companyEmailAddress = companyEmailAddress;
    }

    public long getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(long companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public String getCompanyPhysicalAddress() {
        return companyPhysicalAddress;
    }

    public void setCompanyPhysicalAddress(String companyPhysicalAddress) {
        this.companyPhysicalAddress = companyPhysicalAddress;
    }

    public Timestamp getCreationDate() { return creationDate; }
    public void setCreationDate(Timestamp creationDate) { this.creationDate = creationDate; }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }
}
