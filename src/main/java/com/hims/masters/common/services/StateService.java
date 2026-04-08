package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.StateListRequestDto;
import com.hims.masters.common.dto.request.StateRequestDto;
import org.springframework.http.ResponseEntity;

public interface StateService {
    ResponseEntity<?> saveState(StateRequestDto dto);
    ResponseEntity<?> updateState(StateRequestDto dto);
    ResponseEntity<?> getStateById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> stateDropDown();
    ResponseEntity<?> stateList(StateListRequestDto dto);
    ResponseEntity<?> deleteState(Long id);
}
