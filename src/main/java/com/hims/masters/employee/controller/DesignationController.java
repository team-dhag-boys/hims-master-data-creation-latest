package com.hims.masters.employee.controller;

import com.hims.masters.employee.dto.request.DesignationListRequestDto;
import com.hims.masters.employee.dto.request.DesignationRequestDto;
import com.hims.masters.employee.services.DesignationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/designation")
public class DesignationController {
    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDesignation(@Valid @RequestBody DesignationRequestDto dto) {
        return designationService.saveDesignation(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDesignation(@Valid @RequestBody DesignationRequestDto dto) {
        return designationService.updateDesignation(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getDesignationById(@PathVariable("id") Long id) {
        return designationService.getDesignationById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return designationService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> designationDropDown() {
        return designationService.designationDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> designationList(@RequestBody DesignationListRequestDto dto) {
        return designationService.designationList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDesignation(@PathVariable("id") Long id) {
        return designationService.deleteDesignation(id);
    }
}
