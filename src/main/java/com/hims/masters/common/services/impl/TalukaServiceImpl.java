package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.TalukaListRequestDto;
import com.hims.masters.common.dto.request.TalukaRequestDto;
import com.hims.masters.common.entity.District;
import com.hims.masters.common.entity.Taluka;
import com.hims.masters.common.repository.TalukaRepository;
import com.hims.masters.common.services.TalukaService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TalukaServiceImpl implements TalukaService {
    private final TalukaRepository talukaRepository;
    private final ApiResponseFactory apiResponseFactory;

    public TalukaServiceImpl(TalukaRepository talukaRepository, ApiResponseFactory apiResponseFactory) {
        this.talukaRepository = talukaRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveTaluka(TalukaRequestDto dto) {
        var entity = new Taluka();
        entity.setTalukaCode(dto.getTalukaCode());
        entity.setTalukaName(dto.getTalukaName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        District district = new District();
        district.setId(dto.getDistrictId());
        entity.setDistrict(district);

        talukaRepository.save(entity);
        return apiResponseFactory.ok("Taluka saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateTaluka(TalukaRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Taluka id is null");
        }
        talukaRepository.talukaUpdate(dto.getId(), dto.getTalukaCode(), dto.getTalukaName(), dto.getDistrictId(), dto.getActive());
        return apiResponseFactory.ok("Taluka updated");
    }

    @Override
    public ResponseEntity<?> getTalukaById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Taluka id is null");
        }
        return apiResponseFactory.ok("Taluka details by id", talukaRepository.talukaGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Taluka details found", talukaRepository.talukaAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> talukaDropDown() {
        var list = talukaRepository.talukaDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Taluka list", list, null);
        }
        return apiResponseFactory.notFound("Taluka list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> talukaList(TalukaListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = talukaRepository.talukaListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Taluka list found", talukaRepository.talukaListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Taluka list not found");
    }

    @Override
    public ResponseEntity<?> deleteTaluka(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Taluka id is null");
        }
        talukaRepository.talukaDelete(id);
        return apiResponseFactory.ok("Taluka deleted");
    }
}
