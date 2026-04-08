package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query(value = "select * from fn_mt_department_dropdown()", nativeQuery = true)
    List<Map<String, Object>> departmentDropDown();

    @Query(value = "select * from fn_mt_department_update(?1,?2,?3,?4)", nativeQuery = true)
    void departmentUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_department_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> departmentGetById(Long id);

    @Query(value = "select * from fn_mt_department_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> departmentAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_department_listing_count(?1)", nativeQuery = true)
    Long departmentListingCount(String searchString);

    @Query(value = "select * from fn_mt_department_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> departmentListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_department_delete(?1)", nativeQuery = true)
    void departmentDelete(Long id);
}
