package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.OrganizationListRequestDto;
import com.hims.masters.common.dto.request.OrganizationRequestDto;
import com.hims.masters.common.entity.Organization;
import com.hims.masters.common.repository.OrganizationRepository;
import com.hims.masters.common.services.OrganizationService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final ApiResponseFactory apiResponseFactory;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, ApiResponseFactory apiResponseFactory) {
        this.organizationRepository = organizationRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveOrganization(OrganizationRequestDto dto) {
        var entity = new Organization();
        entity.setOrganizationCode(dto.getOrganizationCode());
        entity.setOrganizationName(dto.getOrganizationName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        organizationRepository.save(entity);
        return apiResponseFactory.ok("Organization saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOrganization(OrganizationRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Organization id is null");
        }
        organizationRepository.organizationUpdate(dto.getId(), dto.getOrganizationCode(), dto.getOrganizationName(), dto.getActive());
        return apiResponseFactory.ok("Organization updated");
    }

    @Override
    public ResponseEntity<?> getOrganizationById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Organization id is null");
        }
        return apiResponseFactory.ok("Organization details by id", organizationRepository.organizationGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Organization details found", organizationRepository.organizationAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> organizationDropDown() {
        var list = organizationRepository.organizationDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Organization list", list, null);
        }
        return apiResponseFactory.notFound("Organization list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> organizationList(OrganizationListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = organizationRepository.organizationListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Organization list found", organizationRepository.organizationListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Organization list not found");
    }

    @Override
    public ResponseEntity<?> deleteOrganization(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Organization id is null");
        }
        organizationRepository.organizationDelete(id);
        return apiResponseFactory.ok("Organization deleted");
    }
}
