package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.DepartmentUnitMappingListRequestDto;
import com.hims.masters.common.dto.request.DepartmentUnitMappingRequestDto;
import org.springframework.http.ResponseEntity;

public interface DepartmentUnitMappingService {
    ResponseEntity<?> saveDepartmentUnitMapping(DepartmentUnitMappingRequestDto dto);
    ResponseEntity<?> updateDepartmentUnitMapping(DepartmentUnitMappingRequestDto dto);
    ResponseEntity<?> getDepartmentUnitMappingById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> departmentUnitMappingDropDown();
    ResponseEntity<?> departmentUnitMappingList(DepartmentUnitMappingListRequestDto dto);
    ResponseEntity<?> deleteDepartmentUnitMapping(Long id);
}
