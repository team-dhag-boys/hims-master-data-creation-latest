package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.TalukaListRequestDto;
import com.hims.masters.common.dto.request.TalukaRequestDto;
import org.springframework.http.ResponseEntity;

public interface TalukaService {
    ResponseEntity<?> saveTaluka(TalukaRequestDto dto);
    ResponseEntity<?> updateTaluka(TalukaRequestDto dto);
    ResponseEntity<?> getTalukaById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> talukaDropDown();
    ResponseEntity<?> talukaList(TalukaListRequestDto dto);
    ResponseEntity<?> deleteTaluka(Long id);
}
