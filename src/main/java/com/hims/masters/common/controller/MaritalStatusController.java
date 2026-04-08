package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.MaritalStatusListRequestDto;
import com.hims.masters.common.dto.request.MaritalStatusRequestDto;
import com.hims.masters.common.services.MaritalStatusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maritalStatus")
public class MaritalStatusController {
    private final MaritalStatusService maritalStatusService;

    public MaritalStatusController(MaritalStatusService maritalStatusService) {
        this.maritalStatusService = maritalStatusService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMaritalStatus(@Valid @RequestBody MaritalStatusRequestDto dto) {
        return maritalStatusService.saveMaritalStatus(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMaritalStatus(@Valid @RequestBody MaritalStatusRequestDto dto) {
        return maritalStatusService.updateMaritalStatus(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getMaritalStatusById(@PathVariable("id") Long id) {
        return maritalStatusService.getMaritalStatusById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return maritalStatusService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> maritalStatusDropDown() {
        return maritalStatusService.maritalStatusDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> maritalStatusList(@RequestBody MaritalStatusListRequestDto dto) {
        return maritalStatusService.maritalStatusList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaritalStatus(@PathVariable("id") Long id) {
        return maritalStatusService.deleteMaritalStatus(id);
    }
}
