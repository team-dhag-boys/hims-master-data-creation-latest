package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.DistrictListRequestDto;
import com.hims.masters.common.dto.request.DistrictRequestDto;
import com.hims.masters.common.entity.District;
import com.hims.masters.common.entity.State;
import com.hims.masters.common.repository.DistrictRepository;
import com.hims.masters.common.services.DistrictService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final ApiResponseFactory apiResponseFactory;

    public DistrictServiceImpl(DistrictRepository districtRepository, ApiResponseFactory apiResponseFactory) {
        this.districtRepository = districtRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveDistrict(DistrictRequestDto dto) {
        var entity = new District();
        entity.setDistrictCode(dto.getDistrictCode());
        entity.setDistrictName(dto.getDistrictName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        State state = new State();
        state.setId(dto.getStateId());
        entity.setState(state);

        districtRepository.save(entity);
        return apiResponseFactory.ok("District saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateDistrict(DistrictRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("District id is null");
        }
        districtRepository.districtUpdate(dto.getId(), dto.getDistrictCode(), dto.getDistrictName(), dto.getStateId(), dto.getActive());
        return apiResponseFactory.ok("District updated");
    }

    @Override
    public ResponseEntity<?> getDistrictById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("District id is null");
        }
        return apiResponseFactory.ok("District details by id", districtRepository.districtGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("District details found", districtRepository.districtAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> districtDropDown() {
        var list = districtRepository.districtDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("District list", list, null);
        }
        return apiResponseFactory.notFound("District list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> districtList(DistrictListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = districtRepository.districtListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("District list found", districtRepository.districtListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("District list not found");
    }

    @Override
    public ResponseEntity<?> deleteDistrict(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("District id is null");
        }
        districtRepository.districtDelete(id);
        return apiResponseFactory.ok("District deleted");
    }
}
