package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.NationalityListRequestDto;
import com.hims.masters.common.dto.request.NationalityRequestDto;
import com.hims.masters.common.services.NationalityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nationality")
public class NationalityController {
    private final NationalityService nationalityService;

    public NationalityController(NationalityService nationalityService) {
        this.nationalityService = nationalityService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveNationality(@Valid @RequestBody NationalityRequestDto dto) {
        return nationalityService.saveNationality(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNationality(@Valid @RequestBody NationalityRequestDto dto) {
        return nationalityService.updateNationality(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getNationalityById(@PathVariable("id") Long id) {
        return nationalityService.getNationalityById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return nationalityService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> nationalityDropDown() {
        return nationalityService.nationalityDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> nationalityList(@RequestBody NationalityListRequestDto dto) {
        return nationalityService.nationalityList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNationality(@PathVariable("id") Long id) {
        return nationalityService.deleteNationality(id);
    }
}
