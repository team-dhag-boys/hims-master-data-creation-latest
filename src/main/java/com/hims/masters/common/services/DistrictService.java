package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.DistrictListRequestDto;
import com.hims.masters.common.dto.request.DistrictRequestDto;
import org.springframework.http.ResponseEntity;

public interface DistrictService {
    ResponseEntity<?> saveDistrict(DistrictRequestDto dto);
    ResponseEntity<?> updateDistrict(DistrictRequestDto dto);
    ResponseEntity<?> getDistrictById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> districtDropDown();
    ResponseEntity<?> districtList(DistrictListRequestDto dto);
    ResponseEntity<?> deleteDistrict(Long id);
}
