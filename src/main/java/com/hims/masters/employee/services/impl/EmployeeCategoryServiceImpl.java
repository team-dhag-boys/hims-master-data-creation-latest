package com.hims.masters.employee.services.impl;

import com.hims.masters.employee.dto.request.EmployeeCategoryListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeCategoryRequestDto;
import com.hims.masters.employee.entity.EmployeeCategory;
import com.hims.masters.employee.entity.EmployeeType;
import com.hims.masters.employee.repository.EmployeeCategoryRepository;
import com.hims.masters.employee.services.EmployeeCategoryService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeCategoryServiceImpl implements EmployeeCategoryService {
    private final EmployeeCategoryRepository employeeCategoryRepository;
    private final ApiResponseFactory apiResponseFactory;

    public EmployeeCategoryServiceImpl(EmployeeCategoryRepository employeeCategoryRepository, ApiResponseFactory apiResponseFactory) {
        this.employeeCategoryRepository = employeeCategoryRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveEmployeeCategory(EmployeeCategoryRequestDto dto) {
        var entity = new EmployeeCategory();
        entity.setCategory(dto.getCategory());
        entity.setCode(dto.getCode());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        if (dto.getEmployeeTypeId() != null) {
            var type = new EmployeeType();
            type.setId(dto.getEmployeeTypeId());
            entity.setEmployeeType(type);
        }
        employeeCategoryRepository.save(entity);
        return apiResponseFactory.ok("Employee category saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateEmployeeCategory(EmployeeCategoryRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Employee category id is null");
        }
        employeeCategoryRepository.employeeCategoryUpdate(
                dto.getId(),
                dto.getCategory(),
                dto.getCode(),
                dto.getEmployeeTypeId(),
                dto.getActive()
        );
        return apiResponseFactory.ok("Employee category updated");
    }

    @Override
    public ResponseEntity<?> getEmployeeCategoryById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee category id is null");
        }
        return apiResponseFactory.ok("Employee category details by id", employeeCategoryRepository.employeeCategoryGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null) {
            return apiResponseFactory.badRequest("Employee category details not found");
        }
        return apiResponseFactory.ok("Employee category details found", employeeCategoryRepository.employeeCategoryAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> employeeCategoryDropDown() {
        var list = employeeCategoryRepository.employeeCategoryDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Employee category list", list, null);
        }
        return apiResponseFactory.notFound("Employee category list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> employeeCategoryList(EmployeeCategoryListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }

        Long count = employeeCategoryRepository.employeeCategoryListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Employee category list found", employeeCategoryRepository.employeeCategoryListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Employee category list not found");
    }

    @Override
    public ResponseEntity<?> deleteEmployeeCategory(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee category id is null");
        }
        employeeCategoryRepository.employeeCategoryDelete(id);
        return apiResponseFactory.ok("Employee category deleted");
    }
}
