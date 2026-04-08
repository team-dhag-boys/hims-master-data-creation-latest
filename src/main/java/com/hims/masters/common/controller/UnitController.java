package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.UnitListRequestDto;
import com.hims.masters.common.dto.request.UnitRequestDto;
import com.hims.masters.common.services.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unit")
public class UnitController {
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUnit(@Valid @RequestBody UnitRequestDto dto) {
        return unitService.saveUnit(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUnit(@Valid @RequestBody UnitRequestDto dto) {
        return unitService.updateUnit(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUnitById(@PathVariable("id") Long id) {
        return unitService.getUnitById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return unitService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> unitDropDown() {
        return unitService.unitDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> unitList(@RequestBody UnitListRequestDto dto) {
        return unitService.unitList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable("id") Long id) {
        return unitService.deleteUnit(id);
    }
}
