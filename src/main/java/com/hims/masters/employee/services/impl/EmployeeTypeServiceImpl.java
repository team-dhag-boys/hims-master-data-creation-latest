package com.hims.masters.employee.services.impl;

import com.hims.masters.employee.dto.request.EmployeeTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeTypeRequestDto;
import com.hims.masters.employee.entity.EmployeeType;
import com.hims.masters.employee.repository.EmployeeTypeRepository;
import com.hims.masters.employee.services.EmployeeTypeService;
import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeTypeServiceImpl implements EmployeeTypeService {
    private final EmployeeTypeRepository employeeTypeRepository;
    private final ApiResponseFactory apiResponseFactory;

    public EmployeeTypeServiceImpl(EmployeeTypeRepository employeeTypeRepository, ApiResponseFactory apiResponseFactory) {
        this.employeeTypeRepository = employeeTypeRepository;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveEmployeeType(EmployeeTypeRequestDto dto) {
        var entity = new EmployeeType();
        entity.setEmployeeType(dto.getEmployeeType());
        entity.setIsClinical(dto.getIsClinical());
        entity.setApplicableToDoctorType(Boolean.TRUE.equals(dto.getApplicableToDoctorType()));
        entity.setIsDoctor(Boolean.TRUE.equals(dto.getIsDoctor()));
        entity.setActive(dto.getActive() == null || dto.getActive());
        entity.setDeleteFlag(false);

        employeeTypeRepository.save(entity);
        return apiResponseFactory.ok("Employee type saved");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateEmployeeType(EmployeeTypeRequestDto dto) {
        if (dto.getId() == null) {
            return apiResponseFactory.badRequest("Employee type id is null");
        }

        employeeTypeRepository.employeeTypeUpdate(
                dto.getId(),
                dto.getEmployeeType(),
                dto.getIsClinical(),
                dto.getApplicableToDoctorType(),
                dto.getIsDoctor(),
                dto.getActive()
        );
        return apiResponseFactory.ok("Employee type updated");
    }

    @Override
    public ResponseEntity<?> getEmployeeTypeById(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee type id is null");
        }
        return apiResponseFactory.ok("Employee type details by id", employeeTypeRepository.employeeTypeGetById(id), null);
    }

    @Override
    public ResponseEntity<?> autocomplete(String searchString) {
        if (searchString == null) {
            return apiResponseFactory.badRequest("Employee type details not found");
        }
        return apiResponseFactory.ok("Employee type details found", employeeTypeRepository.employeeTypeAutocomplete(searchString), null);
    }

    @Override
    public ResponseEntity<?> employeeTypeDropDown() {
        var list = employeeTypeRepository.employeeTypeDropDown();
        if (!list.isEmpty()) {
            return apiResponseFactory.ok("Employee type list", list, null);
        }
        return apiResponseFactory.notFound("Employee type list not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> employeeTypeList(EmployeeTypeListRequestDto dto) {
        Integer page = dto.getPage() == null ? 0 : dto.getPage();
        Integer size = dto.getSize() == null || dto.getSize() <= 0 ? 20 : Math.min(dto.getSize(), 100);
        if (page < 0) {
            return apiResponseFactory.badRequest("Page should be greater than or equal to 0");
        }

        Long count = employeeTypeRepository.employeeTypeListingCount(dto.getSearchString());
        if (count != null && count > 0) {
            return apiResponseFactory.ok("Employee type list found", employeeTypeRepository.employeeTypeListing(page, size, dto.getSearchString()), count);
        }
        return apiResponseFactory.badRequest("Employee type list not found");
    }

    @Override
    public ResponseEntity<?> deleteEmployeeType(Long id) {
        if (id == null) {
            return apiResponseFactory.badRequest("Employee type id is null");
        }
        employeeTypeRepository.employeeTypeDelete(id);
        return apiResponseFactory.ok("Employee type deleted");
    }
}
