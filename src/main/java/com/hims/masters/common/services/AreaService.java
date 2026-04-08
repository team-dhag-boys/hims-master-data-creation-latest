package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.AreaListRequestDto;
import com.hims.masters.common.dto.request.AreaRequestDto;
import org.springframework.http.ResponseEntity;

public interface AreaService {
    ResponseEntity<?> saveArea(AreaRequestDto dto);
    ResponseEntity<?> updateArea(AreaRequestDto dto);
    ResponseEntity<?> getAreaById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> areaDropDown();
    ResponseEntity<?> areaList(AreaListRequestDto dto);
    ResponseEntity<?> deleteArea(Long id);
}
