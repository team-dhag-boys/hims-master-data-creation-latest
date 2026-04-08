package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.AreaListRequestDto;
import com.hims.masters.common.dto.request.AreaRequestDto;
import com.hims.masters.common.services.AreaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/area")
public class AreaController {
    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveArea(@Valid @RequestBody AreaRequestDto dto) {
        return areaService.saveArea(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateArea(@Valid @RequestBody AreaRequestDto dto) {
        return areaService.updateArea(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAreaById(@PathVariable("id") Long id) {
        return areaService.getAreaById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return areaService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> areaDropDown() {
        return areaService.areaDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> areaList(@RequestBody AreaListRequestDto dto) {
        return areaService.areaList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArea(@PathVariable("id") Long id) {
        return areaService.deleteArea(id);
    }
}
