package com.hims.masters.employee.repository;

import com.hims.masters.employee.entity.EmployeeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EmployeeCategoryRepository extends JpaRepository<EmployeeCategory, Long> {
    @Query(value = "select * from fn_mt_employee_category_dropdown()", nativeQuery = true)
    List<Map<String, Object>> employeeCategoryDropDown();

    @Query(value = "select * from fn_mt_employee_category_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void employeeCategoryUpdate(Long id, String category, String code, Long employeeTypeId, Boolean active);

    @Query(value = "select * from fn_mt_employee_category_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> employeeCategoryGetById(Long id);

    @Query(value = "select * from fn_mt_employee_category_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> employeeCategoryAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_employee_category_listing_count(?1)", nativeQuery = true)
    Long employeeCategoryListingCount(String searchString);

    @Query(value = "select * from fn_mt_employee_category_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> employeeCategoryListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_employee_category_delete(?1)", nativeQuery = true)
    void employeeCategoryDelete(Long id);
}
