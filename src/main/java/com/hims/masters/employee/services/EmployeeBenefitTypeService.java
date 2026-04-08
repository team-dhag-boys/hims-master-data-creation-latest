package com.hims.masters.employee.services;

import com.hims.masters.employee.dto.request.EmployeeBenefitTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeBenefitTypeRequestDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeBenefitTypeService {
    ResponseEntity<?> saveEmployeeBenefitType(EmployeeBenefitTypeRequestDto dto);
    ResponseEntity<?> updateEmployeeBenefitType(EmployeeBenefitTypeRequestDto dto);
    ResponseEntity<?> getEmployeeBenefitTypeById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> employeeBenefitTypeDropDown();
    ResponseEntity<?> employeeBenefitTypeList(EmployeeBenefitTypeListRequestDto dto);
    ResponseEntity<?> deleteEmployeeBenefitType(Long id);
}
