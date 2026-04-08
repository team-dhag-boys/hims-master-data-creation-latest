package com.hims.masters.employee.controller;

import com.hims.masters.employee.dto.request.EmployeeBenefitTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeBenefitTypeRequestDto;
import com.hims.masters.employee.services.EmployeeBenefitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeBenefitType")
public class EmployeeBenefitTypeController {
    private final EmployeeBenefitTypeService employeeBenefitTypeService;

    public EmployeeBenefitTypeController(EmployeeBenefitTypeService employeeBenefitTypeService) {
        this.employeeBenefitTypeService = employeeBenefitTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveEmployeeBenefitType(@Valid @RequestBody EmployeeBenefitTypeRequestDto dto) {
        return employeeBenefitTypeService.saveEmployeeBenefitType(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployeeBenefitType(@Valid @RequestBody EmployeeBenefitTypeRequestDto dto) {
        return employeeBenefitTypeService.updateEmployeeBenefitType(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getEmployeeBenefitTypeById(@PathVariable("id") Long id) {
        return employeeBenefitTypeService.getEmployeeBenefitTypeById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return employeeBenefitTypeService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> employeeBenefitTypeDropDown() {
        return employeeBenefitTypeService.employeeBenefitTypeDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> employeeBenefitTypeList(@RequestBody EmployeeBenefitTypeListRequestDto dto) {
        return employeeBenefitTypeService.employeeBenefitTypeList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeBenefitType(@PathVariable("id") Long id) {
        return employeeBenefitTypeService.deleteEmployeeBenefitType(id);
    }
}
