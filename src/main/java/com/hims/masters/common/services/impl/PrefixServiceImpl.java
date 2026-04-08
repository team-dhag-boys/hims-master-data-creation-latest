package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.PrefixListRequestDto;
import com.hims.masters.common.dto.request.PrefixRequestDto;
import com.hims.masters.common.entity.Prefix;
import com.hims.masters.common.repository.PrefixRepository;
import com.hims.masters.common.services.PrefixService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PrefixServiceImpl implements PrefixService {
    private final PrefixRepository prefixRepository;
    private final ApiResponseFactory apiResponseFactory;

    public PrefixServiceImpl(PrefixRepository prefixRepository, ApiResponseFactory apiResponseFactory) {
        this.prefixRepository = prefixRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    public ResponseEntity<?> prefixDropDown() {
        List<Map<String, Object>> list = prefixRepository.getPrefixDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Prefix List", list, null);
        }
        return apiResponseFactory.notFound("Prefix List not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> savePrefix(PrefixRequestDto dto) {
        var prefix = new Prefix();
        prefix.setPrefixName(dto.getPrefixName());
        prefix.setPrefixCode(dto.getPrefixCode());
        prefix.setActive(true);
        prefix.setDeleteFlag(false);
        prefix.setGenderPrefix(dto.getGenderPrefix());

        prefixRepository.save(prefix);
        return apiResponseFactory.ok("Prefix saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePrefix(PrefixRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Prefix id is null");
        }
        prefixRepository.prefixUpdate(dto.getId(), dto.getPrefixCode(), dto.getPrefixName(), dto.getActive(), dto.getGenderPrefix() != null ? dto.getGenderPrefix().getId() : null);
        return apiResponseFactory.ok("Prefix updated");
    }

    @Override
    public ResponseEntity<?> getPrefixById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Prefix id is null");
        }
        Map<String, Object> result = prefixRepository.prefixGetById(id);
        if (result == null || result.isEmpty()) {
            return apiResponseFactory.notFound("Prefix details not found");
        }
        return apiResponseFactory.ok("Prefix details by id", result, null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        List<Map<String, Object>> result = prefixRepository.prefixAutocomplete(searchString);
        if (result == null || result.isEmpty()) {
            return apiResponseFactory.notFound("Prefix details not found");
        }
        return apiResponseFactory.ok("Prefix details found", result, null);
    }

    @Override
    @Transactional
    public ResponseEntity<?> prefixList(PrefixListRequestDto dto) {
        int page = dto.getPage() < 0 ? -1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);

        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }

        Long count = prefixRepository.prefixListingCount(dto.getSearchString());

        if (count == null || count <= 0) {
            return apiResponseFactory.notFound("Prefix list not found");
        }
        return apiResponseFactory.ok("Prefix list found", prefixRepository.prefixListing(page, size, dto.getSearchString()), count);
    }

    @Override
    public ResponseEntity<?> deletePrefix(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Prefix id is null");
        }
        prefixRepository.prefixDelete(id);
        return apiResponseFactory.ok("Prefix deleted");
    }

}
