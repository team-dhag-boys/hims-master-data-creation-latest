package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.BankListRequestDto;
import com.hims.masters.common.dto.request.BankRequestDto;
import com.hims.masters.common.entity.Bank;
import com.hims.masters.common.repository.BankRepository;
import com.hims.masters.common.services.BankService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final ApiResponseFactory apiResponseFactory;

    public BankServiceImpl(BankRepository bankRepository, ApiResponseFactory apiResponseFactory) {
        this.bankRepository = bankRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveBank(BankRequestDto dto) {
        var entity = new Bank();
        entity.setBankCode(dto.getBankCode());
        entity.setBankName(dto.getBankName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        bankRepository.save(entity);
        return apiResponseFactory.ok("Bank saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateBank(BankRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Bank id is null");
        }
        bankRepository.bankUpdate(dto.getId(), dto.getBankCode(), dto.getBankName(), dto.getActive());
        return apiResponseFactory.ok("Bank updated");
    }

    @Override
    public ResponseEntity<?> getBankById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Bank id is null");
        }
        return apiResponseFactory.ok("Bank details by id", bankRepository.bankGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Bank details found", bankRepository.bankAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> bankDropDown() {
        var list = bankRepository.bankDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Bank list", list, null);
        }
        return apiResponseFactory.notFound("Bank list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> bankList(BankListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = bankRepository.bankListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Bank list found", bankRepository.bankListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Bank list not found");
    }

    @Override
    public ResponseEntity<?> deleteBank(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Bank id is null");
        }
        bankRepository.bankDelete(id);
        return apiResponseFactory.ok("Bank deleted");
    }
}
