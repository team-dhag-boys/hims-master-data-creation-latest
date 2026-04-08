package com.hims.masters.employee.services.impl;

import com.hims.masters.employee.dto.request.DesignationListRequestDto;
import com.hims.masters.employee.dto.request.DesignationRequestDto;
import com.hims.masters.employee.entity.Designation;
import com.hims.masters.employee.repository.DesignationRepository;
import com.hims.masters.employee.services.DesignationService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DesignationServiceImpl implements DesignationService {
    private final DesignationRepository designationRepository;
    private final ApiResponseFactory apiResponseFactory;

    public DesignationServiceImpl(DesignationRepository designationRepository, ApiResponseFactory apiResponseFactory) {
        this.designationRepository = designationRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveDesignation(DesignationRequestDto dto) {
        var entity = new Designation();
        entity.setDesignation(dto.getDesignation());
        entity.setCode(dto.getCode());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        designationRepository.save(entity);
        return apiResponseFactory.ok("Designation saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateDesignation(DesignationRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Designation id is null");
        }
        designationRepository.designationUpdate(dto.getId(), dto.getDesignation(), dto.getCode(), dto.getActive());
        return apiResponseFactory.ok("Designation updated");
    }

    @Override
    public ResponseEntity<?> getDesignationById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Designation id is null");
        }
        return apiResponseFactory.ok("Designation details by id", designationRepository.designationGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Designation details found", designationRepository.designationAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> designationDropDown() {
        var list = designationRepository.designationDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Designation list", list, null);
        }
        return apiResponseFactory.notFound("Designation list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> designationList(DesignationListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = designationRepository.designationListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Designation list found", designationRepository.designationListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Designation list not found");
    }

    @Override
    public ResponseEntity<?> deleteDesignation(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Designation id is null");
        }
        designationRepository.designationDelete(id);
        return apiResponseFactory.ok("Designation deleted");
    }
}
