package com.hims.masters.common.services;

import com.hims.masters.common.dto.request.OrganizationListRequestDto;
import com.hims.masters.common.dto.request.OrganizationRequestDto;
import org.springframework.http.ResponseEntity;

public interface OrganizationService {
    ResponseEntity<?> saveOrganization(OrganizationRequestDto dto);
    ResponseEntity<?> updateOrganization(OrganizationRequestDto dto);
    ResponseEntity<?> getOrganizationById(Long id);
    ResponseEntity<?> autocomplete(String searchString);
    ResponseEntity<?> organizationDropDown();
    ResponseEntity<?> organizationList(OrganizationListRequestDto dto);
    ResponseEntity<?> deleteOrganization(Long id);
}
