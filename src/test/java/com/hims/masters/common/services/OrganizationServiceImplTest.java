package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.OrganizationListRequestDto;
import com.hims.masters.common.dto.request.OrganizationRequestDto;
import com.hims.masters.common.repository.OrganizationRepository;
import com.hims.masters.common.services.impl.OrganizationServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrganizationServiceImplTest {
    private final OrganizationRepository repository = mock(OrganizationRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final OrganizationServiceImpl service = new OrganizationServiceImpl(repository, apiResponseFactory);

    @Test
    void saveOrganization_happyPath() {
        OrganizationRequestDto dto = new OrganizationRequestDto();
        dto.setOrganizationCode("HIMS");
        dto.setOrganizationName("HIMS Hospital");

        ResponseEntity<?> responseEntity = service.saveOrganization(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateOrganization_validationFailure_nullId() {
        OrganizationRequestDto dto = new OrganizationRequestDto();
        dto.setOrganizationCode("HIMS");
        dto.setOrganizationName("HIMS Hospital");

        ResponseEntity<?> responseEntity = service.updateOrganization(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Organization id is null", response.getMessage());
    }

    @Test
    void organizationDropDown_negative_emptyList() {
        when(repository.organizationDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.organizationDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void organizationList_validationFailure_negativePage() {
        OrganizationListRequestDto dto = new OrganizationListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.organizationList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteOrganization_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteOrganization(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).organizationDelete(1L);
    }
}
