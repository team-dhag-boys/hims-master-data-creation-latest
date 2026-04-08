package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.PinCodeListRequestDto;
import com.hims.masters.common.dto.request.PinCodeRequestDto;
import com.hims.masters.common.entity.City;
import com.hims.masters.common.entity.PinCode;
import com.hims.masters.common.repository.PinCodeRepository;
import com.hims.masters.common.services.PinCodeService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PinCodeServiceImpl implements PinCodeService {
    private final PinCodeRepository pinCodeRepository;
    private final ApiResponseFactory apiResponseFactory;

    public PinCodeServiceImpl(PinCodeRepository pinCodeRepository, ApiResponseFactory apiResponseFactory) {
        this.pinCodeRepository = pinCodeRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> savePinCode(PinCodeRequestDto dto) {
        var entity = new PinCode();
        entity.setPincode(dto.getPincode());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        City city = new City();
        city.setId(dto.getCityId());
        entity.setCity(city);

        pinCodeRepository.save(entity);
        return apiResponseFactory.ok("Pincode saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePinCode(PinCodeRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Pincode id is null");
        }
        pinCodeRepository.pinCodeUpdate(dto.getId(), dto.getPincode(), dto.getCityId(), dto.getActive());
        return apiResponseFactory.ok("Pincode updated");
    }

    @Override
    public ResponseEntity<?> getPinCodeById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Pincode id is null");
        }
        return apiResponseFactory.ok("Pincode details by id", pinCodeRepository.pinCodeGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Pincode details found", pinCodeRepository.pinCodeAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> pinCodeDropDown() {
        var list = pinCodeRepository.pinCodeDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Pincode list", list, null);
        }
        return apiResponseFactory.notFound("Pincode list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> pinCodeList(PinCodeListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = pinCodeRepository.pinCodeListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Pincode list found", pinCodeRepository.pinCodeListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Pincode list not found");
    }

    @Override
    public ResponseEntity<?> deletePinCode(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Pincode id is null");
        }
        pinCodeRepository.pinCodeDelete(id);
        return apiResponseFactory.ok("Pincode deleted");
    }
}
