package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.CityListRequestDto;
import com.hims.masters.common.dto.request.CityRequestDto;
import com.hims.masters.common.services.CityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCity(@Valid @RequestBody CityRequestDto dto) {
        return cityService.saveCity(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCity(@Valid @RequestBody CityRequestDto dto) {
        return cityService.updateCity(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getCityById(@PathVariable("id") Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return cityService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> cityDropDown() {
        return cityService.cityDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> cityList(@RequestBody CityListRequestDto dto) {
        return cityService.cityList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable("id") Long id) {
        return cityService.deleteCity(id);
    }
}
