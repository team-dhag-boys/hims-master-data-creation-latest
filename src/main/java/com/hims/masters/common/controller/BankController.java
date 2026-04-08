package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.BankListRequestDto;
import com.hims.masters.common.dto.request.BankRequestDto;
import com.hims.masters.common.services.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBank(@Valid @RequestBody BankRequestDto dto) {
        return bankService.saveBank(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBank(@Valid @RequestBody BankRequestDto dto) {
        return bankService.updateBank(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getBankById(@PathVariable("id") Long id) {
        return bankService.getBankById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return bankService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> bankDropDown() {
        return bankService.bankDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> bankList(@RequestBody BankListRequestDto dto) {
        return bankService.bankList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable("id") Long id) {
        return bankService.deleteBank(id);
    }
}
