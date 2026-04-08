package com.hims.masters.employee.services;

import com.hims.masters.employee.dto.request.EmployeeTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeTypeRequestDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeTypeService {
    ResponseEntity<?> saveEmployeeType(EmployeeTypeRequestDto dto);
    ResponseEntity<?> updateEmployeeType(EmployeeTypeRequestDto dto);
    ResponseEntity<?> getEmployeeTypeById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> employeeTypeDropDown();
    ResponseEntity<?> employeeTypeList(EmployeeTypeListRequestDto dto);
    ResponseEntity<?> deleteEmployeeType(Long id);
}
