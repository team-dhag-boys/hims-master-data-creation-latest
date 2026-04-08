package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.MaritalStatusListRequestDto;
import com.hims.masters.common.dto.request.MaritalStatusRequestDto;
import org.springframework.http.ResponseEntity;

public interface MaritalStatusService {
    ResponseEntity<?> saveMaritalStatus(MaritalStatusRequestDto dto);
    ResponseEntity<?> updateMaritalStatus(MaritalStatusRequestDto dto);
    ResponseEntity<?> getMaritalStatusById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> maritalStatusDropDown();
    ResponseEntity<?> maritalStatusList(MaritalStatusListRequestDto dto);
    ResponseEntity<?> deleteMaritalStatus(Long id);
}
