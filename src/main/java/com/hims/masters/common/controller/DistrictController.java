package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.DistrictListRequestDto;
import com.hims.masters.common.dto.request.DistrictRequestDto;
import com.hims.masters.common.services.DistrictService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/district")
public class DistrictController {
    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDistrict(@Valid @RequestBody DistrictRequestDto dto) {
        return districtService.saveDistrict(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDistrict(@Valid @RequestBody DistrictRequestDto dto) {
        return districtService.updateDistrict(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getDistrictById(@PathVariable("id") Long id) {
        return districtService.getDistrictById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return districtService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> districtDropDown() {
        return districtService.districtDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> districtList(@RequestBody DistrictListRequestDto dto) {
        return districtService.districtList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDistrict(@PathVariable("id") Long id) {
        return districtService.deleteDistrict(id);
    }
}
