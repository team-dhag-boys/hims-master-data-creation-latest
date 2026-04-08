package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.DepartmentListRequestDto;
import com.hims.masters.common.dto.request.DepartmentRequestDto;
import org.springframework.http.ResponseEntity;

public interface DepartmentService {
    ResponseEntity<?> saveDepartment(DepartmentRequestDto dto);
    ResponseEntity<?> updateDepartment(DepartmentRequestDto dto);
    ResponseEntity<?> getDepartmentById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> departmentDropDown();
    ResponseEntity<?> departmentList(DepartmentListRequestDto dto);
    ResponseEntity<?> deleteDepartment(Long id);
}
