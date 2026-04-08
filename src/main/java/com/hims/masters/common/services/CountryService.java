package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.CountryListRequestDto;
import com.hims.masters.common.dto.request.CountryRequestDto;
import org.springframework.http.ResponseEntity;

public interface CountryService {
    ResponseEntity<?> saveCountry(CountryRequestDto dto);
    ResponseEntity<?> updateCountry(CountryRequestDto dto);
    ResponseEntity<?> getCountryById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> countryDropDown();
    ResponseEntity<?> countryList(CountryListRequestDto dto);
    ResponseEntity<?> deleteCountry(Long id);
}
