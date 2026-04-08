package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.PinCodeListRequestDto;
import com.hims.masters.common.dto.request.PinCodeRequestDto;
import org.springframework.http.ResponseEntity;

public interface PinCodeService {
    ResponseEntity<?> savePinCode(PinCodeRequestDto dto);
    ResponseEntity<?> updatePinCode(PinCodeRequestDto dto);
    ResponseEntity<?> getPinCodeById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> pinCodeDropDown();
    ResponseEntity<?> pinCodeList(PinCodeListRequestDto dto);
    ResponseEntity<?> deletePinCode(Long id);
}
