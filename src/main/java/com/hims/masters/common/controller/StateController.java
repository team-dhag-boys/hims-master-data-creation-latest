package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.StateListRequestDto;
import com.hims.masters.common.dto.request.StateRequestDto;
import com.hims.masters.common.services.StateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state")
public class StateController {
    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveState(@Valid @RequestBody StateRequestDto dto) {
        return stateService.saveState(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateState(@Valid @RequestBody StateRequestDto dto) {
        return stateService.updateState(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getStateById(@PathVariable("id") Long id) {
        return stateService.getStateById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return stateService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> stateDropDown() {
        return stateService.stateDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> stateList(@RequestBody StateListRequestDto dto) {
        return stateService.stateList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteState(@PathVariable("id") Long id) {
        return stateService.deleteState(id);
    }
}
