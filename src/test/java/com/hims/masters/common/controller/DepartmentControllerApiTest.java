package com.hims.masters.common.controller;

import com.hims.masters.common.services.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
@Import(ApiResponseFactory.class)
class DepartmentControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllDepartmentApis() throws Exception {
        doReturn(okResponse()).when(departmentService).saveDepartment(any());
        doReturn(okResponse()).when(departmentService).updateDepartment(any());
        doReturn(okResponse()).when(departmentService).getDepartmentById(anyLong());
        doReturn(okResponse()).when(departmentService).autocomplete(anyString());
        doReturn(okResponse()).when(departmentService).departmentDropDown();
        doReturn(okResponse()).when(departmentService).departmentList(any());
        doReturn(okResponse()).when(departmentService).deleteDepartment(anyLong());

        mockMvc.perform(post("/department/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"departmentCode\":\"MED\",\"departmentName\":\"Medicine\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/department/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"departmentCode\":\"MED\",\"departmentName\":\"Medicine\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/department/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/department/autocomplete").param("searchString", "Med"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/department/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/department/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Med\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/department/delete/1"))
                .andExpect(status().isOk());
    }
}
