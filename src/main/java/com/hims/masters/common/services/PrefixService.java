package com.hims.masters.common.services;


import com.hims.masters.common.dto.request.PrefixListRequestDto;
import com.hims.masters.common.dto.request.PrefixRequestDto;
import org.springframework.http.ResponseEntity;

public interface PrefixService {

    ResponseEntity<?> prefixDropDown();

    ResponseEntity<?> savePrefix(PrefixRequestDto dto);

    ResponseEntity<?> updatePrefix(PrefixRequestDto dto);

    ResponseEntity<?> getPrefixById(Long id);

    ResponseEntity<?> autocomplete(String searchString);

    ResponseEntity<?> prefixList(PrefixListRequestDto dto);
}
