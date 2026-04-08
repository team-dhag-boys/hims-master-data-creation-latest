package com.hims.masters.employee.repository;

import com.hims.masters.employee.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    @Query(value = "select * from fn_mt_designation_dropdown()", nativeQuery = true)
    List<Map<String, Object>> designationDropDown();

    @Query(value = "select * from fn_mt_designation_update(?1,?2,?3,?4)", nativeQuery = true)
    void designationUpdate(Long id, String designation, String code, Boolean active);

    @Query(value = "select * from fn_mt_designation_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> designationGetById(Long id);

    @Query(value = "select * from fn_mt_designation_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> designationAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_designation_listing_count(?1)", nativeQuery = true)
    Long designationListingCount(String searchString);

    @Query(value = "select * from fn_mt_designation_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> designationListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_designation_delete(?1)", nativeQuery = true)
    void designationDelete(Long id);
}
