package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.CountryListRequestDto;
import com.hims.masters.common.dto.request.CountryRequestDto;
import com.hims.masters.common.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCountry(@Valid @RequestBody CountryRequestDto dto) {
        return countryService.saveCountry(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCountry(@Valid @RequestBody CountryRequestDto dto) {
        return countryService.updateCountry(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getCountryById(@PathVariable("id") Long id) {
        return countryService.getCountryById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return countryService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> countryDropDown() {
        return countryService.countryDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> countryList(@RequestBody CountryListRequestDto dto) {
        return countryService.countryList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable("id") Long id) {
        return countryService.deleteCountry(id);
    }
}
