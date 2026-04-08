package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.PinCodeListRequestDto;
import com.hims.masters.common.dto.request.PinCodeRequestDto;
import com.hims.masters.common.services.PinCodeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pincode")
public class PinCodeController {
    private final PinCodeService pinCodeService;

    public PinCodeController(PinCodeService pinCodeService) {
        this.pinCodeService = pinCodeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePinCode(@Valid @RequestBody PinCodeRequestDto dto) {
        return pinCodeService.savePinCode(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePinCode(@Valid @RequestBody PinCodeRequestDto dto) {
        return pinCodeService.updatePinCode(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPinCodeById(@PathVariable("id") Long id) {
        return pinCodeService.getPinCodeById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return pinCodeService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> pinCodeDropDown() {
        return pinCodeService.pinCodeDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> pinCodeList(@RequestBody PinCodeListRequestDto dto) {
        return pinCodeService.pinCodeList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePinCode(@PathVariable("id") Long id) {
        return pinCodeService.deletePinCode(id);
    }
}
