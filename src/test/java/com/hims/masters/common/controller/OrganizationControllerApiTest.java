package com.hims.masters.common.controller;

import com.hims.masters.common.services.OrganizationService;
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

@WebMvcTest(OrganizationController.class)
@Import(ApiResponseFactory.class)
class OrganizationControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService organizationService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllOrganizationApis() throws Exception {
        doReturn(okResponse()).when(organizationService).saveOrganization(any());
        doReturn(okResponse()).when(organizationService).updateOrganization(any());
        doReturn(okResponse()).when(organizationService).getOrganizationById(anyLong());
        doReturn(okResponse()).when(organizationService).autocomplete(anyString());
        doReturn(okResponse()).when(organizationService).organizationDropDown();
        doReturn(okResponse()).when(organizationService).organizationList(any());
        doReturn(okResponse()).when(organizationService).deleteOrganization(anyLong());

        mockMvc.perform(post("/organization/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"organizationCode\":\"HIMS\",\"organizationName\":\"HIMS Hospital\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/organization/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"organizationCode\":\"HIMS\",\"organizationName\":\"HIMS Hospital\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/organization/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/organization/autocomplete").param("searchString", "HIM"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/organization/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/organization/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"HIM\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/organization/delete/1"))
                .andExpect(status().isOk());
    }
}
