package com.hims.masters.common.controller;

import com.hims.masters.common.services.DistrictService;
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

@WebMvcTest(DistrictController.class)
@Import(ApiResponseFactory.class)
class DistrictControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistrictService districtService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllDistrictApis() throws Exception {
        doReturn(okResponse()).when(districtService).saveDistrict(any());
        doReturn(okResponse()).when(districtService).updateDistrict(any());
        doReturn(okResponse()).when(districtService).getDistrictById(anyLong());
        doReturn(okResponse()).when(districtService).autocomplete(anyString());
        doReturn(okResponse()).when(districtService).districtDropDown();
        doReturn(okResponse()).when(districtService).districtList(any());
        doReturn(okResponse()).when(districtService).deleteDistrict(anyLong());

        mockMvc.perform(post("/district/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"districtCode\":\"PUN\",\"districtName\":\"Pune\",\"stateId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/district/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"districtCode\":\"PUN\",\"districtName\":\"Pune\",\"stateId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/district/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/district/autocomplete").param("searchString", "Pun"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/district/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/district/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Pun\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/district/delete/1"))
                .andExpect(status().isOk());
    }
}
