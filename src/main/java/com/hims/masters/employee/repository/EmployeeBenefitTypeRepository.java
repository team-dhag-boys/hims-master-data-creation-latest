package com.hims.masters.employee.repository;

import com.hims.masters.employee.entity.EmployeeBenefitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EmployeeBenefitTypeRepository extends JpaRepository<EmployeeBenefitType, Long> {
    @Query(value = "select * from fn_mt_employee_benefit_type_dropdown()", nativeQuery = true)
    List<Map<String, Object>> employeeBenefitTypeDropDown();

    @Query(value = "select * from fn_mt_employee_benefit_type_update(?1,?2,?3,?4)", nativeQuery = true)
    void employeeBenefitTypeUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_employee_benefit_type_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> employeeBenefitTypeGetById(Long id);

    @Query(value = "select * from fn_mt_employee_benefit_type_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> employeeBenefitTypeAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_employee_benefit_type_listing_count(?1)", nativeQuery = true)
    Long employeeBenefitTypeListingCount(String searchString);

    @Query(value = "select * from fn_mt_employee_benefit_type_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> employeeBenefitTypeListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_employee_benefit_type_delete(?1)", nativeQuery = true)
    void employeeBenefitTypeDelete(Long id);
}
