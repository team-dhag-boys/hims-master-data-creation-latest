package com.hims.masters.common.controller;

import com.hims.masters.common.dto.request.OrganizationListRequestDto;
import com.hims.masters.common.dto.request.OrganizationRequestDto;
import com.hims.masters.common.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrganization(@Valid @RequestBody OrganizationRequestDto dto) {
        return organizationService.saveOrganization(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody OrganizationRequestDto dto) {
        return organizationService.updateOrganization(dto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable("id") Long id) {
        return organizationService.getOrganizationById(id);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("searchString") String searchString) {
        return organizationService.autocomplete(searchString);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<?> organizationDropDown() {
        return organizationService.organizationDropDown();
    }

    @PostMapping("/list")
    public ResponseEntity<?> organizationList(@RequestBody OrganizationListRequestDto dto) {
        return organizationService.organizationList(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable("id") Long id) {
        return organizationService.deleteOrganization(id);
    }
}
