package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.UnitListRequestDto;
import com.hims.masters.common.dto.request.UnitRequestDto;
import com.hims.masters.common.entity.Organization;
import com.hims.masters.common.entity.Unit;
import com.hims.masters.common.repository.UnitRepository;
import com.hims.masters.common.services.UnitService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final ApiResponseFactory apiResponseFactory;

    public UnitServiceImpl(UnitRepository unitRepository, ApiResponseFactory apiResponseFactory) {
        this.unitRepository = unitRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveUnit(UnitRequestDto dto) {
        var entity = new Unit();
        entity.setUnitCode(dto.getUnitCode());
        entity.setUnitName(dto.getUnitName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        Organization org = new Organization();
        org.setId(dto.getOrganizationId());
        entity.setOrganization(org);
        unitRepository.save(entity);
        return apiResponseFactory.ok("Unit saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUnit(UnitRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Unit id is null");
        }
        unitRepository.unitUpdate(dto.getId(), dto.getUnitCode(), dto.getUnitName(), dto.getOrganizationId(), dto.getActive());
        return apiResponseFactory.ok("Unit updated");
    }

    @Override
    public ResponseEntity<?> getUnitById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Unit id is null");
        }
        return apiResponseFactory.ok("Unit details by id", unitRepository.unitGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Unit details found", unitRepository.unitAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> unitDropDown() {
        var list = unitRepository.unitDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Unit list", list, null);
        }
        return apiResponseFactory.notFound("Unit list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> unitList(UnitListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = unitRepository.unitListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Unit list found", unitRepository.unitListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Unit list not found");
    }

    @Override
    public ResponseEntity<?> deleteUnit(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Unit id is null");
        }
        unitRepository.unitDelete(id);
        return apiResponseFactory.ok("Unit deleted");
    }
}
