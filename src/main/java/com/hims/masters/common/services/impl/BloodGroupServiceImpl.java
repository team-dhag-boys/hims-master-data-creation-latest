package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.BloodGroupListRequestDto;
import com.hims.masters.common.dto.request.BloodGroupRequestDto;
import com.hims.masters.common.repository.BloodGroupRepository;
import com.hims.masters.common.services.BloodGroupService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BloodGroupServiceImpl implements BloodGroupService {
    private final BloodGroupRepository bloodGroupRepository;
    private final ApiResponseFactory apiResponseFactory;

    public BloodGroupServiceImpl(BloodGroupRepository bloodGroupRepository, ApiResponseFactory apiResponseFactory) {
        this.bloodGroupRepository = bloodGroupRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveBloodGroup(BloodGroupRequestDto dto) {
        return apiResponseFactory.badRequest("Blood group is a system master and cannot be created manually");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateBloodGroup(BloodGroupRequestDto dto) {
        return apiResponseFactory.badRequest("Blood group is a system master and cannot be updated manually");
    }

    @Override
    public ResponseEntity<?> getBloodGroupById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Blood group id is null");
        }
        return apiResponseFactory.ok("Blood group details by id", bloodGroupRepository.bloodGroupGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null) {
            return apiResponseFactory.badRequest("Blood group details not found");
        }
        return apiResponseFactory.ok("Blood group details found", bloodGroupRepository.bloodGroupAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> bloodGroupDropDown() {
        var list = bloodGroupRepository.bloodGroupDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Blood group list", list, null);
        }
        return apiResponseFactory.notFound("Blood group list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> bloodGroupList(BloodGroupListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = bloodGroupRepository.bloodGroupListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Blood group list found", bloodGroupRepository.bloodGroupListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Blood group list not found");
    }

    @Override
    public ResponseEntity<?> deleteBloodGroup(Long id) {
        return apiResponseFactory.badRequest("Blood group is a system master and cannot be deleted manually");
    }
}
