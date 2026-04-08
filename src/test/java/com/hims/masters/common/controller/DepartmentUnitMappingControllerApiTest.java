package com.hims.masters.common.controller;

import com.hims.masters.common.services.DepartmentUnitMappingService;
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

@WebMvcTest(DepartmentUnitMappingController.class)
@Import(ApiResponseFactory.class)
class DepartmentUnitMappingControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentUnitMappingService departmentUnitMappingService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllDepartmentUnitMappingApis() throws Exception {
        doReturn(okResponse()).when(departmentUnitMappingService).saveDepartmentUnitMapping(any());
        doReturn(okResponse()).when(departmentUnitMappingService).updateDepartmentUnitMapping(any());
        doReturn(okResponse()).when(departmentUnitMappingService).getDepartmentUnitMappingById(anyLong());
        doReturn(okResponse()).when(departmentUnitMappingService).autocomplete(anyString());
        doReturn(okResponse()).when(departmentUnitMappingService).departmentUnitMappingDropDown();
        doReturn(okResponse()).when(departmentUnitMappingService).departmentUnitMappingList(any());
        doReturn(okResponse()).when(departmentUnitMappingService).deleteDepartmentUnitMapping(anyLong());

        mockMvc.perform(post("/departmentUnitMapping/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"departmentId\":1,\"unitId\":2}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/departmentUnitMapping/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"departmentId\":1,\"unitId\":2,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/departmentUnitMapping/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/departmentUnitMapping/autocomplete").param("searchString", "Med"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/departmentUnitMapping/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/departmentUnitMapping/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Med\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/departmentUnitMapping/delete/1"))
                .andExpect(status().isOk());
    }
}
