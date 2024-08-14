package com.vanatel.sidar.DataBaseRepository;

import com.vanatel.sidar.Model.OrganizationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationDetails, Integer> {
    List<OrganizationDetails> findByBuildingId(String buildingId);

    @Query("SELECT o.organizationId FROM OrganizationDetails o ORDER BY o.organizationId DESC LIMIT 1")
    Integer findLastOrganizationId();

    List<OrganizationDetails> findByBuildingIdAndFloorNumber(String buildingID, int floorNumber);
}
