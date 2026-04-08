package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.CountryListRequestDto;
import com.hims.masters.common.dto.request.CountryRequestDto;
import com.hims.masters.common.entity.Country;
import com.hims.masters.common.repository.CountryRepository;
import com.hims.masters.common.services.CountryService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ApiResponseFactory apiResponseFactory;

    public CountryServiceImpl(CountryRepository countryRepository, ApiResponseFactory apiResponseFactory) {
        this.countryRepository = countryRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveCountry(CountryRequestDto dto) {
        var entity = new Country();
        entity.setCountryCode(dto.getCountryCode());
        entity.setCountryName(dto.getCountryName());
        entity.setIsdCode(dto.getIsdCode());
        entity.setMobileLength(dto.getMobileLength());
        entity.setIsDefault(dto.getIsDefault() != null && dto.getIsDefault());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        countryRepository.save(entity);
        return apiResponseFactory.ok("Country saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCountry(CountryRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Country id is null");
        }
        countryRepository.countryUpdate(
                dto.getId(),
                dto.getCountryCode(),
                dto.getCountryName(),
                dto.getIsdCode(),
                dto.getMobileLength(),
                dto.getActive()
        );
        return apiResponseFactory.ok("Country updated");
    }

    @Override
    public ResponseEntity<?> getCountryById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Country id is null");
        }
        return apiResponseFactory.ok("Country details by id", countryRepository.countryGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Country details found", countryRepository.countryAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> countryDropDown() {
        var list = countryRepository.countryDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Country list", list, null);
        }
        return apiResponseFactory.notFound("Country list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> countryList(CountryListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = countryRepository.countryListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Country list found", countryRepository.countryListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Country list not found");
    }

    @Override
    public ResponseEntity<?> deleteCountry(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Country id is null");
        }
        countryRepository.countryDelete(id);
        return apiResponseFactory.ok("Country deleted");
    }
}
