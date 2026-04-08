package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.PrefixListRequestDto;
import com.hims.masters.common.dto.request.PrefixRequestDto;
import com.hims.masters.common.services.PrefixService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/prefix")
public class PrefixController {
    private final PrefixService prefixService;

    public PrefixController(PrefixService prefixService) {
        this.prefixService = prefixService;
    }

    @PostMapping("save")
    public ResponseEntity<?> savePrefix(@Valid @RequestBody PrefixRequestDto dto) {
        return prefixService.savePrefix(dto);
    }

    @PutMapping("update")
    public ResponseEntity<?> updatePrefix(@Valid @RequestBody PrefixRequestDto dto) {
        return prefixService.updatePrefix(dto);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getPrefixById(@PathVariable("id") Long id) {
        return prefixService.getPrefixById(id);
    }

    @GetMapping("autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return prefixService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> prefixDropDown() {
        return prefixService.prefixDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> prefixList(@RequestBody PrefixListRequestDto dto) {
        return prefixService.prefixList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrefix(@PathVariable("id") Long id) {
        return prefixService.deletePrefix(id);
    }

}
