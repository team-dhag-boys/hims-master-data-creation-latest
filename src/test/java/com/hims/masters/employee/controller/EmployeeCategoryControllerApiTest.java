package com.hims.masters.employee.controller;

import com.hims.masters.employee.services.EmployeeCategoryService;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeCategoryController.class)
@Import(ApiResponseFactory.class)
class EmployeeCategoryControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeCategoryService employeeCategoryService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllEmployeeCategoryApis() throws Exception {
        doReturn(okResponse()).when(employeeCategoryService).saveEmployeeCategory(any());
        doReturn(okResponse()).when(employeeCategoryService).updateEmployeeCategory(any());
        doReturn(okResponse()).when(employeeCategoryService).getEmployeeCategoryById(anyLong());
        doReturn(okResponse()).when(employeeCategoryService).autocomplete(anyString());
        doReturn(okResponse()).when(employeeCategoryService).employeeCategoryDropDown();
        doReturn(okResponse()).when(employeeCategoryService).employeeCategoryList(any());
        doReturn(okResponse()).when(employeeCategoryService).deleteEmployeeCategory(anyLong());

        mockMvc.perform(post("/employeeCategory/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"category\":\"Clinical\",\"code\":\"CLN\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/employeeCategory/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"category\":\"Clinical\",\"code\":\"CLN\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeCategory/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeCategory/autocomplete").param("searchString", "CLN"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeCategory/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/employeeCategory/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"CLN\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/employeeCategory/delete/1"))
                .andExpect(status().isOk());
    }
}
