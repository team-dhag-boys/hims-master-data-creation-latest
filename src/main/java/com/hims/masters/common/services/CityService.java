package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.CityListRequestDto;
import com.hims.masters.common.dto.request.CityRequestDto;
import org.springframework.http.ResponseEntity;

public interface CityService {
    ResponseEntity<?> saveCity(CityRequestDto dto);
    ResponseEntity<?> updateCity(CityRequestDto dto);
    ResponseEntity<?> getCityById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> cityDropDown();
    ResponseEntity<?> cityList(CityListRequestDto dto);
    ResponseEntity<?> deleteCity(Long id);
}
