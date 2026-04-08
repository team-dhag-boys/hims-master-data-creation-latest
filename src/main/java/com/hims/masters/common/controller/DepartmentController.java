package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.DepartmentListRequestDto;
import com.hims.masters.common.dto.request.DepartmentRequestDto;
import com.hims.masters.common.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        return departmentService.saveDepartment(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        return departmentService.updateDepartment(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") Long id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return departmentService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> departmentDropDown() {
        return departmentService.departmentDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> departmentList(@RequestBody DepartmentListRequestDto dto) {
        return departmentService.departmentList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable("id") Long id) {
        return departmentService.deleteDepartment(id);
    }
}
