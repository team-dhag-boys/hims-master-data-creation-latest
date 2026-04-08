package com.hims.masters.common.repository;

import com.hims.masters.common.entity.DepartmentUnitMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DepartmentUnitMappingRepository extends JpaRepository<DepartmentUnitMapping, Long> {
    @Query(value = "select * from fn_mt_department_unit_mapping_dropdown()", nativeQuery = true)
    List<Map<String, Object>> departmentUnitMappingDropDown();

    @Query(value = "select * from fn_mt_department_unit_mapping_update(?1,?2,?3,?4)", nativeQuery = true)
    void departmentUnitMappingUpdate(Long id, Long departmentId, Long unitId, Boolean active);

    @Query(value = "select * from fn_mt_department_unit_mapping_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> departmentUnitMappingGetById(Long id);

    @Query(value = "select * from fn_mt_department_unit_mapping_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> departmentUnitMappingAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_department_unit_mapping_listing_count(?1)", nativeQuery = true)
    Long departmentUnitMappingListingCount(String searchString);

    @Query(value = "select * from fn_mt_department_unit_mapping_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> departmentUnitMappingListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_department_unit_mapping_delete(?1)", nativeQuery = true)
    void departmentUnitMappingDelete(Long id);
}
