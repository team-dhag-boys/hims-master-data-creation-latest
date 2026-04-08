package com.hims.masters.common.repository;

import com.hims.masters.common.entity.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {
    @Query(value = "select * from fn_mt_marital_status_dropdown()", nativeQuery = true)
    List<Map<String, Object>> maritalStatusDropDown();

    @Query(value = "select * from fn_mt_marital_status_update(?1,?2,?3,?4)", nativeQuery = true)
    void maritalStatusUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_marital_status_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> maritalStatusGetById(Long id);

    @Query(value = "select * from fn_mt_marital_status_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> maritalStatusAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_marital_status_listing_count(?1)", nativeQuery = true)
    Long maritalStatusListingCount(String searchString);

    @Query(value = "select * from fn_mt_marital_status_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> maritalStatusListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_marital_status_delete(?1)", nativeQuery = true)
    void maritalStatusDelete(Long id);
}
