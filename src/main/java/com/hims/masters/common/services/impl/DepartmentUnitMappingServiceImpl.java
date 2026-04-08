package com.hims.masters.common.services.impl;

import com.hims.masters.common.dto.request.DepartmentUnitMappingListRequestDto;
import com.hims.masters.common.dto.request.DepartmentUnitMappingRequestDto;
import com.hims.masters.common.entity.Department;
import com.hims.masters.common.entity.DepartmentUnitMapping;
import com.hims.masters.common.entity.Unit;
import com.hims.masters.common.repository.DepartmentUnitMappingRepository;
import com.hims.masters.common.services.DepartmentUnitMappingService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentUnitMappingServiceImpl implements DepartmentUnitMappingService {
    private final DepartmentUnitMappingRepository departmentUnitMappingRepository;
    private final ApiResponseFactory apiResponseFactory;

    public DepartmentUnitMappingServiceImpl(DepartmentUnitMappingRepository departmentUnitMappingRepository, ApiResponseFactory apiResponseFactory) {
        this.departmentUnitMappingRepository = departmentUnitMappingRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveDepartmentUnitMapping(DepartmentUnitMappingRequestDto dto) {
        var entity = new DepartmentUnitMapping();
        Department department = new Department();
        department.setId(dto.getDepartmentId());
        entity.setDepartment(department);
        Unit unit = new Unit();
        unit.setId(dto.getUnitId());
        entity.setUnit(unit);
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        departmentUnitMappingRepository.save(entity);
        return apiResponseFactory.ok("Department unit mapping saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateDepartmentUnitMapping(DepartmentUnitMappingRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Department unit mapping id is null");
        }
        departmentUnitMappingRepository.departmentUnitMappingUpdate(dto.getId(), dto.getDepartmentId(), dto.getUnitId(), dto.getActive());
        return apiResponseFactory.ok("Department unit mapping updated");
    }

    @Override
    public ResponseEntity<?> getDepartmentUnitMappingById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Department unit mapping id is null");
        }
        return apiResponseFactory.ok("Department unit mapping details by id", departmentUnitMappingRepository.departmentUnitMappingGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Department unit mapping details found", departmentUnitMappingRepository.departmentUnitMappingAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> departmentUnitMappingDropDown() {
        var list = departmentUnitMappingRepository.departmentUnitMappingDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Department unit mapping list", list, null);
        }
        return apiResponseFactory.notFound("Department unit mapping list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> departmentUnitMappingList(DepartmentUnitMappingListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = departmentUnitMappingRepository.departmentUnitMappingListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Department unit mapping list found", departmentUnitMappingRepository.departmentUnitMappingListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Department unit mapping list not found");
    }

    @Override
    public ResponseEntity<?> deleteDepartmentUnitMapping(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Department unit mapping id is null");
        }
        departmentUnitMappingRepository.departmentUnitMappingDelete(id);
        return apiResponseFactory.ok("Department unit mapping deleted");
    }
}
