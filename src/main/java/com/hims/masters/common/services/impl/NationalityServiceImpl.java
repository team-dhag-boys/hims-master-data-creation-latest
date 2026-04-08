package com.hims.masters.common.services.impl;

import com.hims.masters.aop.NoServiceLog;
import com.hims.masters.common.dto.request.NationalityListRequestDto;
import com.hims.masters.common.dto.request.NationalityRequestDto;
import com.hims.masters.common.entity.Nationality;
import com.hims.masters.common.repository.NationalityRepository;
import com.hims.masters.common.services.NationalityService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NationalityServiceImpl implements NationalityService {
    private final NationalityRepository nationalityRepository;
    private final ApiResponseFactory apiResponseFactory;

    public NationalityServiceImpl(NationalityRepository nationalityRepository, ApiResponseFactory apiResponseFactory) {
        this.nationalityRepository = nationalityRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveNationality(NationalityRequestDto dto) {
        var entity = new Nationality();
        entity.setNationalityCode(dto.getNationalityCode());
        entity.setNationalityName(dto.getNationalityName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        nationalityRepository.save(entity);
        return apiResponseFactory.ok("Nationality saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateNationality(NationalityRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Nationality id is null");
        }
        nationalityRepository.nationalityUpdate(dto.getId(), dto.getNationalityCode(), dto.getNationalityName(), dto.getActive());
        return apiResponseFactory.ok("Nationality updated");
    }

    @Override
    public ResponseEntity<?> getNationalityById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Nationality id is null");
        }
        return apiResponseFactory.ok("Nationality details by id", nationalityRepository.nationalityGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null) {
            return apiResponseFactory.badRequest("Nationality details not found");
        }
        return apiResponseFactory.ok("Nationality details found", nationalityRepository.nationalityAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> nationalityDropDown() {
        var list = nationalityRepository.nationalityDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Nationality list", list, null);
        }
        return apiResponseFactory.notFound("Nationality list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> nationalityList(NationalityListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = nationalityRepository.nationalityListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Nationality list found", nationalityRepository.nationalityListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Nationality list not found");
    }

    @Override
    public ResponseEntity<?> deleteNationality(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Nationality id is null");
        }
        nationalityRepository.nationalityDelete(id);
        return apiResponseFactory.ok("Nationality deleted");
    }
}
