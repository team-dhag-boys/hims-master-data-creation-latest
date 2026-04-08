package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.QualificationListRequestDto;
import com.hims.masters.common.dto.request.QualificationRequestDto;
import com.hims.masters.common.entity.Qualification;
import com.hims.masters.common.repository.QualificationRepository;
import com.hims.masters.common.services.QualificationService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QualificationServiceImpl implements QualificationService {
    private final QualificationRepository qualificationRepository;
    private final ApiResponseFactory apiResponseFactory;

    public QualificationServiceImpl(QualificationRepository qualificationRepository, ApiResponseFactory apiResponseFactory) {
        this.qualificationRepository = qualificationRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveQualification(QualificationRequestDto dto) {
        var entity = new Qualification();
        entity.setQualificationCode(dto.getQualificationCode());
        entity.setQualificationName(dto.getQualificationName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        qualificationRepository.save(entity);
        return apiResponseFactory.ok("Qualification saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateQualification(QualificationRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Qualification id is null");
        }
        qualificationRepository.qualificationUpdate(dto.getId(), dto.getQualificationCode(), dto.getQualificationName(), dto.getActive());
        return apiResponseFactory.ok("Qualification updated");
    }

    @Override
    public ResponseEntity<?> getQualificationById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Qualification id is null");
        }
        return apiResponseFactory.ok("Qualification details by id", qualificationRepository.qualificationGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Qualification details found", qualificationRepository.qualificationAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> qualificationDropDown() {
        var list = qualificationRepository.qualificationDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Qualification list", list, null);
        }
        return apiResponseFactory.notFound("Qualification list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> qualificationList(QualificationListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = qualificationRepository.qualificationListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Qualification list found", qualificationRepository.qualificationListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Qualification list not found");
    }

    @Override
    public ResponseEntity<?> deleteQualification(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Qualification id is null");
        }
        qualificationRepository.qualificationDelete(id);
        return apiResponseFactory.ok("Qualification deleted");
    }
}
