package com.hims.masters.employee.controller;

import com.hims.masters.employee.services.EmployeeTypeService;
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

@WebMvcTest(EmployeeTypeController.class)
@Import(ApiResponseFactory.class)
class EmployeeTypeControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeTypeService employeeTypeService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllEmployeeTypeApis() throws Exception {
        doReturn(okResponse()).when(employeeTypeService).saveEmployeeType(any());
        doReturn(okResponse()).when(employeeTypeService).updateEmployeeType(any());
        doReturn(okResponse()).when(employeeTypeService).getEmployeeTypeById(anyLong());
        doReturn(okResponse()).when(employeeTypeService).autocomplete(anyString());
        doReturn(okResponse()).when(employeeTypeService).employeeTypeDropDown();
        doReturn(okResponse()).when(employeeTypeService).employeeTypeList(any());
        doReturn(okResponse()).when(employeeTypeService).deleteEmployeeType(anyLong());

        mockMvc.perform(post("/employeeType/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"employeeType\":\"Consultant\",\"isClinical\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/employeeType/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"employeeType\":\"Consultant\",\"isClinical\":true,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeType/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeType/autocomplete").param("searchString", "Con"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeType/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/employeeType/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Con\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/employeeType/delete/1"))
                .andExpect(status().isOk());
    }
}
