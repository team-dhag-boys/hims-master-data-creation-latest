package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.NationalityListRequestDto;
import com.hims.masters.common.dto.request.NationalityRequestDto;
import org.springframework.http.ResponseEntity;

public interface NationalityService {
    ResponseEntity<?> saveNationality(NationalityRequestDto dto);
    ResponseEntity<?> updateNationality(NationalityRequestDto dto);
    ResponseEntity<?> getNationalityById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> nationalityDropDown();
    ResponseEntity<?> nationalityList(NationalityListRequestDto dto);
    ResponseEntity<?> deleteNationality(Long id);
}
