package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.UnitListRequestDto;
import com.hims.masters.common.dto.request.UnitRequestDto;
import org.springframework.http.ResponseEntity;

public interface UnitService {
    ResponseEntity<?> saveUnit(UnitRequestDto dto);
    ResponseEntity<?> updateUnit(UnitRequestDto dto);
    ResponseEntity<?> getUnitById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> unitDropDown();
    ResponseEntity<?> unitList(UnitListRequestDto dto);
    ResponseEntity<?> deleteUnit(Long id);
}
