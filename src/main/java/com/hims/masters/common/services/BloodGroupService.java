package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.BloodGroupListRequestDto;
import com.hims.masters.common.dto.request.BloodGroupRequestDto;
import org.springframework.http.ResponseEntity;

public interface BloodGroupService {
    ResponseEntity<?> saveBloodGroup(BloodGroupRequestDto dto);
    ResponseEntity<?> updateBloodGroup(BloodGroupRequestDto dto);
    ResponseEntity<?> getBloodGroupById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> bloodGroupDropDown();
    ResponseEntity<?> bloodGroupList(BloodGroupListRequestDto dto);
    ResponseEntity<?> deleteBloodGroup(Long id);
}
