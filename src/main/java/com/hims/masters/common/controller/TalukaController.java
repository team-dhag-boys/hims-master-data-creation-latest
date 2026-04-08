package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.TalukaListRequestDto;
import com.hims.masters.common.dto.request.TalukaRequestDto;
import com.hims.masters.common.services.TalukaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/taluka")
public class TalukaController {
    private final TalukaService talukaService;

    public TalukaController(TalukaService talukaService) {
        this.talukaService = talukaService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveTaluka(@Valid @RequestBody TalukaRequestDto dto) {
        return talukaService.saveTaluka(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTaluka(@Valid @RequestBody TalukaRequestDto dto) {
        return talukaService.updateTaluka(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getTalukaById(@PathVariable("id") Long id) {
        return talukaService.getTalukaById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return talukaService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> talukaDropDown() {
        return talukaService.talukaDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> talukaList(@RequestBody TalukaListRequestDto dto) {
        return talukaService.talukaList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTaluka(@PathVariable("id") Long id) {
        return talukaService.deleteTaluka(id);
    }
}
