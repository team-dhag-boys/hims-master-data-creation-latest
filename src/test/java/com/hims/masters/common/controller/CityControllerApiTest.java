package com.hims.masters.common.controller;

import com.hims.masters.common.services.CityService;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
@Import(ApiResponseFactory.class)
class CityControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllCityApis() throws Exception {
        doReturn(okResponse()).when(cityService).saveCity(any());
        doReturn(okResponse()).when(cityService).updateCity(any());
        doReturn(okResponse()).when(cityService).getCityById(anyLong());
        doReturn(okResponse()).when(cityService).autocomplete(anyString());
        doReturn(okResponse()).when(cityService).cityDropDown();
        doReturn(okResponse()).when(cityService).cityList(any());
        doReturn(okResponse()).when(cityService).deleteCity(anyLong());

        mockMvc.perform(post("/city/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"cityCode\":\"PUNEC\",\"cityName\":\"Pune City\",\"talukaId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/city/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"cityCode\":\"PUNEC\",\"cityName\":\"Pune City\",\"talukaId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/city/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/city/autocomplete").param("searchString", "Pun"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/city/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/city/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Pun\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/city/delete/1"))
                .andExpect(status().isOk());
    }
}
