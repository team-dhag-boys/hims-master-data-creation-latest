package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.BankListRequestDto;
import com.hims.masters.common.dto.request.BankRequestDto;
import org.springframework.http.ResponseEntity;

public interface BankService {
    ResponseEntity<?> saveBank(BankRequestDto dto);
    ResponseEntity<?> updateBank(BankRequestDto dto);
    ResponseEntity<?> getBankById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> bankDropDown();
    ResponseEntity<?> bankList(BankListRequestDto dto);
    ResponseEntity<?> deleteBank(Long id);
}
