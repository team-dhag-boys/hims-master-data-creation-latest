package com.hims.masters.employee.services.impl;

import com.hims.masters.employee.dto.request.EmployeeBenefitTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeBenefitTypeRequestDto;
import com.hims.masters.employee.entity.EmployeeBenefitType;
import com.hims.masters.employee.repository.EmployeeBenefitTypeRepository;
import com.hims.masters.employee.services.EmployeeBenefitTypeService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeBenefitTypeServiceImpl implements EmployeeBenefitTypeService {
    private final EmployeeBenefitTypeRepository employeeBenefitTypeRepository;
    private final ApiResponseFactory apiResponseFactory;

    public EmployeeBenefitTypeServiceImpl(EmployeeBenefitTypeRepository employeeBenefitTypeRepository, ApiResponseFactory apiResponseFactory) {
        this.employeeBenefitTypeRepository = employeeBenefitTypeRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveEmployeeBenefitType(EmployeeBenefitTypeRequestDto dto) {
        var entity = new EmployeeBenefitType();
        entity.setBenefitTypeCode(dto.getBenefitTypeCode());
        entity.setBenefitTypeName(dto.getBenefitTypeName());
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);
        employeeBenefitTypeRepository.save(entity);
        return apiResponseFactory.ok("Employee benefit type saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateEmployeeBenefitType(EmployeeBenefitTypeRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Employee benefit type id is null");
        }
        employeeBenefitTypeRepository.employeeBenefitTypeUpdate(dto.getId(), dto.getBenefitTypeCode(), dto.getBenefitTypeName(), dto.getActive());
        return apiResponseFactory.ok("Employee benefit type updated");
    }

    @Override
    public ResponseEntity<?> getEmployeeBenefitTypeById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee benefit type id is null");
        }
        return apiResponseFactory.ok("Employee benefit type details by id", employeeBenefitTypeRepository.employeeBenefitTypeGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            return apiResponseFactory.badRequest("Search string is required");
        }
        return apiResponseFactory.ok("Employee benefit type details found", employeeBenefitTypeRepository.employeeBenefitTypeAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> employeeBenefitTypeDropDown() {
        var list = employeeBenefitTypeRepository.employeeBenefitTypeDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Employee benefit type list", list, null);
        }
        return apiResponseFactory.notFound("Employee benefit type list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> employeeBenefitTypeList(EmployeeBenefitTypeListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }
        Long count = employeeBenefitTypeRepository.employeeBenefitTypeListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Employee benefit type list found", employeeBenefitTypeRepository.employeeBenefitTypeListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Employee benefit type list not found");
    }

    @Override
    public ResponseEntity<?> deleteEmployeeBenefitType(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee benefit type id is null");
        }
        employeeBenefitTypeRepository.employeeBenefitTypeDelete(id);
        return apiResponseFactory.ok("Employee benefit type deleted");
    }
}
