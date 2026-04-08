package com.hims.masters.employee.controller;

import com.hims.masters.employee.dto.request.EmployeeTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeTypeRequestDto;
import com.hims.masters.employee.services.EmployeeTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeType")
public class EmployeeTypeController {
    private final EmployeeTypeService employeeTypeService;

    public EmployeeTypeController(EmployeeTypeService employeeTypeService) {
        this.employeeTypeService = employeeTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveEmployeeType(@Valid @RequestBody EmployeeTypeRequestDto dto) {
        return employeeTypeService.saveEmployeeType(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployeeType(@Valid @RequestBody EmployeeTypeRequestDto dto) {
        return employeeTypeService.updateEmployeeType(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getEmployeeTypeById(@PathVariable("id") Long id) {
        return employeeTypeService.getEmployeeTypeById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return employeeTypeService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> employeeTypeDropDown() {
        return employeeTypeService.employeeTypeDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> employeeTypeList(@RequestBody EmployeeTypeListRequestDto dto) {
        return employeeTypeService.employeeTypeList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeType(@PathVariable("id") Long id) {
        return employeeTypeService.deleteEmployeeType(id);
    }
}
