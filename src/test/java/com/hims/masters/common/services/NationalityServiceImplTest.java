package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.NationalityListRequestDto;
import com.hims.masters.common.dto.request.NationalityRequestDto;
import com.hims.masters.common.repository.NationalityRepository;
import com.hims.masters.common.services.impl.NationalityServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NationalityServiceImplTest {
    private final NationalityRepository repository = mock(NationalityRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final NationalityServiceImpl service = new NationalityServiceImpl(repository, apiResponseFactory);

    @Test
    void saveNationality_happyPath() {
        NationalityRequestDto dto = new NationalityRequestDto();
        dto.setNationalityCode("IND");
        dto.setNationalityName("Indian");
        ResponseEntity<?> responseEntity = service.saveNationality(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateNationality_validationFailure_nullId() {
        NationalityRequestDto dto = new NationalityRequestDto();
        dto.setNationalityCode("IND");
        dto.setNationalityName("Indian");
        ResponseEntity<?> responseEntity = service.updateNationality(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Nationality id is null", response.getMessage());
    }

    @Test
    void nationalityDropDown_negative_emptyList() {
        when(repository.nationalityDropDown()).thenReturn(List.of());
        ResponseEntity<?> responseEntity = service.nationalityDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void nationalityList_validationFailure_negativePage() {
        NationalityListRequestDto dto = new NationalityListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);
        ResponseEntity<?> responseEntity = service.nationalityList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteNationality_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteNationality(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).nationalityDelete(1L);
    }
}
