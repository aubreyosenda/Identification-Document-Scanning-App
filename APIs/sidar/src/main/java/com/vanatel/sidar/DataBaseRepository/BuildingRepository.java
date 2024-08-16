package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.BuildingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingDetails, String> {

    @Query("SELECT b.buildingId FROM BuildingDetails b ORDER BY b.buildingId DESC LIMIT 1")
    String findLastBuildingID();

    List<BuildingDetails> findByCompanyID(String companyID);

    boolean existsById(String buildingId);

    @Query("SELECT b.buildingName, b.companyID, c.companyName FROM BuildingDetails b JOIN CompanyDetails c ON b.companyID = c.companyId WHERE b.buildingId = :buildingId")
    Optional<Object[]> findCompanyAndBuildingNamesByBuildingId(@Param("buildingId") String buildingId);
}
