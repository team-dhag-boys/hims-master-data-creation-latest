package com.hims.masters.common.repository;

import com.hims.masters.common.entity.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {
    @Query(value = "select * from fn_mt_blood_group_dropdown()", nativeQuery = true)
    List<Map<String, Object>> bloodGroupDropDown();

    @Query(value = "select * from fn_mt_blood_group_update(?1,?2,?3,?4)", nativeQuery = true)
    void bloodGroupUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_blood_group_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> bloodGroupGetById(Long id);

    @Query(value = "select * from fn_mt_blood_group_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> bloodGroupAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_blood_group_listing_count(?1)", nativeQuery = true)
    Long bloodGroupListingCount(String searchString);

    @Query(value = "select * from fn_mt_blood_group_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> bloodGroupListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_blood_group_delete(?1)", nativeQuery = true)
    void bloodGroupDelete(Long id);
}
