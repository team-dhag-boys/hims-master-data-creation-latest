package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.CityListRequestDto;
import com.hims.masters.common.dto.request.CityRequestDto;
import com.hims.masters.common.entity.City;
import com.hims.masters.common.entity.Taluka;
import com.hims.masters.common.repository.CityRepository;
import com.hims.masters.common.services.CityService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final ApiResponseFactory apiResponseFactory;

    public CityServiceImpl(CityRepository cityRepository, ApiResponseFactory apiResponseFactory) {
        this.cityRepository = cityRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveCity(CityRequestDto dto) {
        var entity = new City();
        entity.setCityCode(dto.getCityCode());
        entity.setCityName(dto.getCityName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        Taluka taluka = new Taluka();
        taluka.setId(dto.getTalukaId());
        entity.setTaluka(taluka);

        cityRepository.save(entity);
        return apiResponseFactory.ok("City saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCity(CityRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("City id is null");
        }
        cityRepository.cityUpdate(dto.getId(), dto.getCityCode(), dto.getCityName(), dto.getTalukaId(), dto.getActive());
        return apiResponseFactory.ok("City updated");
    }

    @Override
    public ResponseEntity<?> getCityById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("City id is null");
        }
        return apiResponseFactory.ok("City details by id", cityRepository.cityGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("City details found", cityRepository.cityAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> cityDropDown() {
        var list = cityRepository.cityDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("City list", list, null);
        }
        return apiResponseFactory.notFound("City list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> cityList(CityListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = cityRepository.cityListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("City list found", cityRepository.cityListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("City list not found");
    }

    @Override
    public ResponseEntity<?> deleteCity(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("City id is null");
        }
        cityRepository.cityDelete(id);
        return apiResponseFactory.ok("City deleted");
    }
}
