package com.hims.masters.employee.repository;

import com.hims.masters.employee.entity.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long> {
    @Query(value = "select * from fn_mt_employee_type_dropdown()", nativeQuery = true)
    List<Map<String, Object>> employeeTypeDropDown();

    @Query(value = "select * from fn_mt_employee_type_update(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    void employeeTypeUpdate(Long id, String employeeType, Boolean isClinical, Boolean applicableToDoctorType, Boolean isDoctor, Boolean active);

    @Query(value = "select * from fn_mt_employee_type_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> employeeTypeGetById(Long id);

    @Query(value = "select * from fn_mt_employee_type_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> employeeTypeAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_employee_type_listing_count(?1)", nativeQuery = true)
    Long employeeTypeListingCount(String searchString);

    @Query(value = "select * from fn_mt_employee_type_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> employeeTypeListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_employee_type_delete(?1)", nativeQuery = true)
    void employeeTypeDelete(Long id);
}
