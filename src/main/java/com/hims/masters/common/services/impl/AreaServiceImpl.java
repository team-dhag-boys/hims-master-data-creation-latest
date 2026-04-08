package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.AreaListRequestDto;
import com.hims.masters.common.dto.request.AreaRequestDto;
import com.hims.masters.common.entity.Area;
import com.hims.masters.common.entity.PinCode;
import com.hims.masters.common.repository.AreaRepository;
import com.hims.masters.common.services.AreaService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final ApiResponseFactory apiResponseFactory;

    public AreaServiceImpl(AreaRepository areaRepository, ApiResponseFactory apiResponseFactory) {
        this.areaRepository = areaRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveArea(AreaRequestDto dto) {
        var entity = new Area();
        entity.setAreaCode(dto.getAreaCode());
        entity.setAreaName(dto.getAreaName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        PinCode pinCode = new PinCode();
        pinCode.setId(dto.getPinCodeId());
        entity.setPinCode(pinCode);

        areaRepository.save(entity);
        return apiResponseFactory.ok("Area saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateArea(AreaRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Area id is null");
        }
        areaRepository.areaUpdate(dto.getId(), dto.getAreaCode(), dto.getAreaName(), dto.getPinCodeId(), dto.getActive());
        return apiResponseFactory.ok("Area updated");
    }

    @Override
    public ResponseEntity<?> getAreaById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Area id is null");
        }
        return apiResponseFactory.ok("Area details by id", areaRepository.areaGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Area details found", areaRepository.areaAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> areaDropDown() {
        var list = areaRepository.areaDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Area list", list, null);
        }
        return apiResponseFactory.notFound("Area list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> areaList(AreaListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = areaRepository.areaListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Area list found", areaRepository.areaListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Area list not found");
    }

    @Override
    public ResponseEntity<?> deleteArea(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Area id is null");
        }
        areaRepository.areaDelete(id);
        return apiResponseFactory.ok("Area deleted");
    }
}
