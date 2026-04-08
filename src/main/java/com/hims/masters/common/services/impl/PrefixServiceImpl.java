package com.hims.masters.common.services.impl;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.PrefixListRequestDto;
import com.hims.masters.common.dto.request.PrefixRequestDto;
import com.hims.masters.common.entity.Prefix;
import com.hims.masters.common.repository.PrefixRepository;
import com.hims.masters.common.services.PrefixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrefixServiceImpl implements PrefixService {
    @Autowired
    private PrefixRepository prefixRepository;

    @Override
    public ResponseEntity<?> prefixDropDown() {
        var response = new ApiResponse<>();
        List<Map<String, Object>> list = prefixRepository.getPrefixDropDown();
        if (!list.isEmpty()) {
            response.setMessage("Prefix List");
            response.setResult(list);
            response.setStatusCode(HttpStatus.OK.value());
        } else {
            response.setMessage("Prefix List not found");
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> savePrefix(PrefixRequestDto dto) {
        var response = new ApiResponse<>();

        var prefix = new Prefix();
        prefix.setPrefixName(dto.getPrefixName());
        prefix.setPrefixCode(dto.getPrefixCode());
        prefix.setActive(true);
        prefix.setDeleteFlag(false);
        prefix.setGenderPrefix(dto.getGenderPrefix());

        prefixRepository.save(prefix);
        response.setMessage("Prefix saved");
        response.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updatePrefix(PrefixRequestDto dto) {
        var response = new ApiResponse<>();

        if (dto.getId() != null) {
            prefixRepository.prefixUpdate(dto.getId(), dto.getPrefixCode(), dto.getPrefixName(), dto.getActive(), dto.getGenderPrefix() != null ? dto.getGenderPrefix().getId() : null);
            response.setMessage("Prefix updated");
            response.setStatusCode(HttpStatus.OK.value());
        } else {
            response.setMessage("Prefix id is null");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getPrefixById(Long id) {
        var response = new ApiResponse<>();

        if (id != null) {
            response.setResult(prefixRepository.prefixGetById(id));
            response.setMessage("Prefix details by id");
            response.setStatusCode(HttpStatus.OK.value());
        } else {
            response.setMessage("Prefix id is null");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        var response = new ApiResponse<>();

        if (searchString != null) {
            response.setResult(prefixRepository.prefixAutocomplete(searchString));
            response.setMessage("Prefix details found");
            response.setStatusCode(HttpStatus.OK.value());
        } else {
            response.setMessage("Prefix details not found");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> prefixList(PrefixListRequestDto dto) {
        var response = new ApiResponse<>();

        Long count = prefixRepository.prefixListingCount(dto.getSearchString());

        if (count > 0) {
            response.setResult(prefixRepository.prefixListing(dto.getPage(), dto.getSize(), dto.getSearchString()));
            response.setMessage("Prefix list found");
            response.setCount(count);
            response.setStatusCode(HttpStatus.OK.value());
        } else {
            response.setMessage("Prefix list not found");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return ResponseEntity.ok(response);
    }

}
