package com.hims.masters.employee.services;

import com.hims.masters.employee.dto.request.EmployeeCategoryListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeCategoryRequestDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeCategoryService {
    ResponseEntity<?> saveEmployeeCategory(EmployeeCategoryRequestDto dto);
    ResponseEntity<?> updateEmployeeCategory(EmployeeCategoryRequestDto dto);
    ResponseEntity<?> getEmployeeCategoryById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> employeeCategoryDropDown();
    ResponseEntity<?> employeeCategoryList(EmployeeCategoryListRequestDto dto);
    ResponseEntity<?> deleteEmployeeCategory(Long id);
}
