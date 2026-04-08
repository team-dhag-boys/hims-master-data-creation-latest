package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.QualificationListRequestDto;
import com.hims.masters.common.dto.request.QualificationRequestDto;
import org.springframework.http.ResponseEntity;

public interface QualificationService {
    ResponseEntity<?> saveQualification(QualificationRequestDto dto);
    ResponseEntity<?> updateQualification(QualificationRequestDto dto);
    ResponseEntity<?> getQualificationById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> qualificationDropDown();
    ResponseEntity<?> qualificationList(QualificationListRequestDto dto);
    ResponseEntity<?> deleteQualification(Long id);
}
