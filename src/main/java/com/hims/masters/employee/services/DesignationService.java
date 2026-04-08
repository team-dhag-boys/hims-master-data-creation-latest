package com.hims.masters.employee.services;

import com.hims.masters.employee.dto.request.DesignationListRequestDto;
import com.hims.masters.employee.dto.request.DesignationRequestDto;
import org.springframework.http.ResponseEntity;

public interface DesignationService {
    ResponseEntity<?> saveDesignation(DesignationRequestDto dto);
    ResponseEntity<?> updateDesignation(DesignationRequestDto dto);
    ResponseEntity<?> getDesignationById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> designationDropDown();
    ResponseEntity<?> designationList(DesignationListRequestDto dto);
    ResponseEntity<?> deleteDesignation(Long id);
}
