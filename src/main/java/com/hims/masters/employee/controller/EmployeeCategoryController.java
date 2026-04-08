package com.hims.masters.employee.controller;

import com.hims.masters.employee.dto.request.EmployeeCategoryListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeCategoryRequestDto;
import com.hims.masters.employee.services.EmployeeCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeCategory")
public class EmployeeCategoryController {
    private final EmployeeCategoryService employeeCategoryService;

    public EmployeeCategoryController(EmployeeCategoryService employeeCategoryService) {
        this.employeeCategoryService = employeeCategoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveEmployeeCategory(@Valid @RequestBody EmployeeCategoryRequestDto dto) {
        return employeeCategoryService.saveEmployeeCategory(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployeeCategory(@Valid @RequestBody EmployeeCategoryRequestDto dto) {
        return employeeCategoryService.updateEmployeeCategory(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getEmployeeCategoryById(@PathVariable("id") Long id) {
        return employeeCategoryService.getEmployeeCategoryById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return employeeCategoryService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> employeeCategoryDropDown() {
        return employeeCategoryService.employeeCategoryDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> employeeCategoryList(@RequestBody EmployeeCategoryListRequestDto dto) {
        return employeeCategoryService.employeeCategoryList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeCategory(@PathVariable("id") Long id) {
        return employeeCategoryService.deleteEmployeeCategory(id);
    }
}
