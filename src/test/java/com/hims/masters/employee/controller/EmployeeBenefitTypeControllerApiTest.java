package com.hims.masters.employee.controller;

import com.hims.masters.employee.services.EmployeeBenefitTypeService;
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

@WebMvcTest(EmployeeBenefitTypeController.class)
@Import(ApiResponseFactory.class)
class EmployeeBenefitTypeControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeBenefitTypeService employeeBenefitTypeService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllEmployeeBenefitTypeApis() throws Exception {
        doReturn(okResponse()).when(employeeBenefitTypeService).saveEmployeeBenefitType(any());
        doReturn(okResponse()).when(employeeBenefitTypeService).updateEmployeeBenefitType(any());
        doReturn(okResponse()).when(employeeBenefitTypeService).getEmployeeBenefitTypeById(anyLong());
        doReturn(okResponse()).when(employeeBenefitTypeService).autocomplete(anyString());
        doReturn(okResponse()).when(employeeBenefitTypeService).employeeBenefitTypeDropDown();
        doReturn(okResponse()).when(employeeBenefitTypeService).employeeBenefitTypeList(any());
        doReturn(okResponse()).when(employeeBenefitTypeService).deleteEmployeeBenefitType(anyLong());

        mockMvc.perform(post("/employeeBenefitType/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"benefitTypeCode\":\"PF\",\"benefitTypeName\":\"Provident Fund\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/employeeBenefitType/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"benefitTypeCode\":\"PF\",\"benefitTypeName\":\"Provident Fund\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeBenefitType/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeBenefitType/autocomplete").param("searchString", "PF"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employeeBenefitType/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/employeeBenefitType/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"PF\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/employeeBenefitType/delete/1"))
                .andExpect(status().isOk());
    }
}
