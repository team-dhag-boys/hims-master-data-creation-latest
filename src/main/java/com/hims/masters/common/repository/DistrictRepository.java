package com.hims.masters.common.repository;

import com.hims.masters.common.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DistrictRepository extends JpaRepository<District, Long> {
    @Query(value = "select * from fn_mt_district_dropdown()", nativeQuery = true)
    List<Map<String, Object>> districtDropDown();

    @Query(value = "select * from fn_mt_district_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void districtUpdate(Long id, String code, String name, Long stateId, Boolean active);

    @Query(value = "select * from fn_mt_district_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> districtGetById(Long id);

    @Query(value = "select * from fn_mt_district_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> districtAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_district_listing_count(?1)", nativeQuery = true)
    Long districtListingCount(String searchString);

    @Query(value = "select * from fn_mt_district_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> districtListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_district_delete(?1)", nativeQuery = true)
    void districtDelete(Long id);
}
