package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.DistrictListRequestDto;
import com.hims.masters.common.dto.request.DistrictRequestDto;
import com.hims.masters.common.repository.DistrictRepository;
import com.hims.masters.common.services.impl.DistrictServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DistrictServiceImplTest {
    private final DistrictRepository repository = mock(DistrictRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final DistrictServiceImpl service = new DistrictServiceImpl(repository, apiResponseFactory);

    @Test
    void saveDistrict_happyPath() {
        DistrictRequestDto dto = new DistrictRequestDto();
        dto.setDistrictCode("PUN");
        dto.setDistrictName("Pune");
        dto.setStateId(1L);

        ResponseEntity<?> responseEntity = service.saveDistrict(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateDistrict_validationFailure_nullId() {
        DistrictRequestDto dto = new DistrictRequestDto();
        dto.setDistrictCode("PUN");
        dto.setDistrictName("Pune");
        dto.setStateId(1L);

        ResponseEntity<?> responseEntity = service.updateDistrict(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("District id is null", response.getMessage());
    }

    @Test
    void districtDropDown_negative_emptyList() {
        when(repository.districtDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.districtDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void districtList_validationFailure_negativePage() {
        DistrictListRequestDto dto = new DistrictListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.districtList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteDistrict_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteDistrict(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).districtDelete(1L);
    }
}
