package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.MaritalStatusListRequestDto;
import com.hims.masters.common.dto.request.MaritalStatusRequestDto;
import com.hims.masters.common.entity.MaritalStatus;
import com.hims.masters.common.repository.MaritalStatusRepository;
import com.hims.masters.common.services.MaritalStatusService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaritalStatusServiceImpl implements MaritalStatusService {
    private final MaritalStatusRepository maritalStatusRepository;
    private final ApiResponseFactory apiResponseFactory;

    public MaritalStatusServiceImpl(MaritalStatusRepository maritalStatusRepository, ApiResponseFactory apiResponseFactory) {
        this.maritalStatusRepository = maritalStatusRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveMaritalStatus(MaritalStatusRequestDto dto) {
        var entity = new MaritalStatus();
        entity.setMaritalStatusCode(dto.getMaritalStatusCode());
        entity.setMaritalStatusName(dto.getMaritalStatusName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        maritalStatusRepository.save(entity);
        return apiResponseFactory.ok("Marital status saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateMaritalStatus(MaritalStatusRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Marital status id is null");
        }
        maritalStatusRepository.maritalStatusUpdate(dto.getId(), dto.getMaritalStatusCode(), dto.getMaritalStatusName(), dto.getActive());
        return apiResponseFactory.ok("Marital status updated");
    }

    @Override
    public ResponseEntity<?> getMaritalStatusById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Marital status id is null");
        }
        return apiResponseFactory.ok("Marital status details by id", maritalStatusRepository.maritalStatusGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null) {
            return apiResponseFactory.badRequest("Marital status details not found");
        }
        return apiResponseFactory.ok("Marital status details found", maritalStatusRepository.maritalStatusAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> maritalStatusDropDown() {
        var list = maritalStatusRepository.maritalStatusDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Marital status list", list, null);
        }
        return apiResponseFactory.notFound("Marital status list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> maritalStatusList(MaritalStatusListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = maritalStatusRepository.maritalStatusListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Marital status list found", maritalStatusRepository.maritalStatusListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Marital status list not found");
    }

    @Override
    public ResponseEntity<?> deleteMaritalStatus(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Marital status id is null");
        }
        maritalStatusRepository.maritalStatusDelete(id);
        return apiResponseFactory.ok("Marital status deleted");
    }
}
