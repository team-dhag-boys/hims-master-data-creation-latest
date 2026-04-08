package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.QualificationListRequestDto;
import com.hims.masters.common.dto.request.QualificationRequestDto;
import com.hims.masters.common.services.QualificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qualification")
public class QualificationController {
    private final QualificationService qualificationService;

    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveQualification(@Valid @RequestBody QualificationRequestDto dto) {
        return qualificationService.saveQualification(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateQualification(@Valid @RequestBody QualificationRequestDto dto) {
        return qualificationService.updateQualification(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getQualificationById(@PathVariable("id") Long id) {
        return qualificationService.getQualificationById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return qualificationService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> qualificationDropDown() {
        return qualificationService.qualificationDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> qualificationList(@RequestBody QualificationListRequestDto dto) {
        return qualificationService.qualificationList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQualification(@PathVariable("id") Long id) {
        return qualificationService.deleteQualification(id);
    }
}
