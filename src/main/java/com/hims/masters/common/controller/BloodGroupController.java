package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.BloodGroupListRequestDto;
import com.hims.masters.common.dto.request.BloodGroupRequestDto;
import com.hims.masters.common.services.BloodGroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bloodGroup")
public class BloodGroupController {
    private final BloodGroupService bloodGroupService;

    public BloodGroupController(BloodGroupService bloodGroupService) {
        this.bloodGroupService = bloodGroupService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBloodGroup(@Valid @RequestBody BloodGroupRequestDto dto) {
        return bloodGroupService.saveBloodGroup(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBloodGroup(@Valid @RequestBody BloodGroupRequestDto dto) {
        return bloodGroupService.updateBloodGroup(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getBloodGroupById(@PathVariable("id") Long id) {
        return bloodGroupService.getBloodGroupById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return bloodGroupService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> bloodGroupDropDown() {
        return bloodGroupService.bloodGroupDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> bloodGroupList(@RequestBody BloodGroupListRequestDto dto) {
        return bloodGroupService.bloodGroupList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBloodGroup(@PathVariable("id") Long id) {
        return bloodGroupService.deleteBloodGroup(id);
    }
}
