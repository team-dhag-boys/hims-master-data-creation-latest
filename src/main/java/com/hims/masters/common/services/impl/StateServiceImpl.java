package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.StateListRequestDto;
import com.hims.masters.common.dto.request.StateRequestDto;
import com.hims.masters.common.entity.Country;
import com.hims.masters.common.entity.State;
import com.hims.masters.common.repository.StateRepository;
import com.hims.masters.common.services.StateService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;
    private final ApiResponseFactory apiResponseFactory;

    public StateServiceImpl(StateRepository stateRepository, ApiResponseFactory apiResponseFactory) {
        this.stateRepository = stateRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveState(StateRequestDto dto) {
        var entity = new State();
        entity.setStateCode(dto.getStateCode());
        entity.setStateName(dto.getStateName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        Country country = new Country();
        country.setId(dto.getCountryId());
        entity.setCountry(country);

        stateRepository.save(entity);
        return apiResponseFactory.ok("State saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateState(StateRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("State id is null");
        }
        stateRepository.stateUpdate(dto.getId(), dto.getStateCode(), dto.getStateName(), dto.getCountryId(), dto.getActive());
        return apiResponseFactory.ok("State updated");
    }

    @Override
    public ResponseEntity<?> getStateById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("State id is null");
        }
        return apiResponseFactory.ok("State details by id", stateRepository.stateGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("State details found", stateRepository.stateAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> stateDropDown() {
        var list = stateRepository.stateDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("State list", list, null);
        }
        return apiResponseFactory.notFound("State list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> stateList(StateListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = stateRepository.stateListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("State list found", stateRepository.stateListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("State list not found");
    }

    @Override
    public ResponseEntity<?> deleteState(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("State id is null");
        }
        stateRepository.stateDelete(id);
        return apiResponseFactory.ok("State deleted");
    }
}
