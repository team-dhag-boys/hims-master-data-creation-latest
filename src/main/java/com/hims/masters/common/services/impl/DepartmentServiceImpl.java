package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.DepartmentListRequestDto;
import com.hims.masters.common.dto.request.DepartmentRequestDto;
import com.hims.masters.common.entity.Department;
import com.hims.masters.common.repository.DepartmentRepository;
import com.hims.masters.common.services.DepartmentService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ApiResponseFactory apiResponseFactory;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ApiResponseFactory apiResponseFactory) {
        this.departmentRepository = departmentRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveDepartment(DepartmentRequestDto dto) {
        var entity = new Department();
        entity.setDepartmentCode(dto.getDepartmentCode());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        departmentRepository.save(entity);
        return apiResponseFactory.ok("Department saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateDepartment(DepartmentRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Department id is null");
        }
        departmentRepository.departmentUpdate(dto.getId(), dto.getDepartmentCode(), dto.getDepartmentName(), dto.getActive());
        return apiResponseFactory.ok("Department updated");
    }

    @Override
    public ResponseEntity<?> getDepartmentById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Department id is null");
        }
        return apiResponseFactory.ok("Department details by id", departmentRepository.departmentGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Department details found", departmentRepository.departmentAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> departmentDropDown() {
        var list = departmentRepository.departmentDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Department list", list, null);
        }
        return apiResponseFactory.notFound("Department list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> departmentList(DepartmentListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = departmentRepository.departmentListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Department list found", departmentRepository.departmentListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Department list not found");
    }

    @Override
    public ResponseEntity<?> deleteDepartment(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Department id is null");
        }
        departmentRepository.departmentDelete(id);
        return apiResponseFactory.ok("Department deleted");
    }
}
