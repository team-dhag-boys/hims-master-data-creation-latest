package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.DepartmentUnitMappingListRequestDto;
import com.hims.masters.common.dto.request.DepartmentUnitMappingRequestDto;
import com.hims.masters.common.services.DepartmentUnitMappingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departmentUnitMapping")
public class DepartmentUnitMappingController {
    private final DepartmentUnitMappingService departmentUnitMappingService;

    public DepartmentUnitMappingController(DepartmentUnitMappingService departmentUnitMappingService) {
        this.departmentUnitMappingService = departmentUnitMappingService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDepartmentUnitMapping(@Valid @RequestBody DepartmentUnitMappingRequestDto dto) {
        return departmentUnitMappingService.saveDepartmentUnitMapping(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDepartmentUnitMapping(@Valid @RequestBody DepartmentUnitMappingRequestDto dto) {
        return departmentUnitMappingService.updateDepartmentUnitMapping(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getDepartmentUnitMappingById(@PathVariable("id") Long id) {
        return departmentUnitMappingService.getDepartmentUnitMappingById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return departmentUnitMappingService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> departmentUnitMappingDropDown() {
        return departmentUnitMappingService.departmentUnitMappingDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> departmentUnitMappingList(@RequestBody DepartmentUnitMappingListRequestDto dto) {
        return departmentUnitMappingService.departmentUnitMappingList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartmentUnitMapping(@PathVariable("id") Long id) {
        return departmentUnitMappingService.deleteDepartmentUnitMapping(id);
    }
}
