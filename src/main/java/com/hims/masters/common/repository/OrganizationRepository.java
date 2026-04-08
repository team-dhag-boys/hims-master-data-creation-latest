package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query(value = "select * from fn_mt_organization_dropdown()", nativeQuery = true)
    List<Map<String, Object>> organizationDropDown();

    @Query(value = "select * from fn_mt_organization_update(?1,?2,?3,?4)", nativeQuery = true)
    void organizationUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_organization_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> organizationGetById(Long id);

    @Query(value = "select * from fn_mt_organization_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> organizationAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_organization_listing_count(?1)", nativeQuery = true)
    Long organizationListingCount(String searchString);

    @Query(value = "select * from fn_mt_organization_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> organizationListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_organization_delete(?1)", nativeQuery = true)
    void organizationDelete(Long id);
}
