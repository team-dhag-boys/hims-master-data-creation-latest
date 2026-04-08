package com.hims.masters.common.controller;

import com.hims.masters.common.services.CountryService;
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

@WebMvcTest(CountryController.class)
@Import(ApiResponseFactory.class)
class CountryControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllCountryApis() throws Exception {
        doReturn(okResponse()).when(countryService).saveCountry(any());
        doReturn(okResponse()).when(countryService).updateCountry(any());
        doReturn(okResponse()).when(countryService).getCountryById(anyLong());
        doReturn(okResponse()).when(countryService).autocomplete(anyString());
        doReturn(okResponse()).when(countryService).countryDropDown();
        doReturn(okResponse()).when(countryService).countryList(any());
        doReturn(okResponse()).when(countryService).deleteCountry(anyLong());

        mockMvc.perform(post("/country/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"countryCode\":\"IN\",\"countryName\":\"India\",\"isdCode\":\"+91\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/country/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"countryCode\":\"IN\",\"countryName\":\"India\",\"isdCode\":\"+91\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/country/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/country/autocomplete").param("searchString", "Ind"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/country/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/country/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Ind\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/country/delete/1"))
                .andExpect(status().isOk());
    }
}
