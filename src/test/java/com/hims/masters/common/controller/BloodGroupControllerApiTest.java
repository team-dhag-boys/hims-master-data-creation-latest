package com.hims.masters.common.controller;

import com.hims.masters.common.services.BloodGroupService;
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

@WebMvcTest(BloodGroupController.class)
@Import(ApiResponseFactory.class)
class BloodGroupControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BloodGroupService bloodGroupService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllBloodGroupApis() throws Exception {
        doReturn(okResponse()).when(bloodGroupService).saveBloodGroup(any());
        doReturn(okResponse()).when(bloodGroupService).updateBloodGroup(any());
        doReturn(okResponse()).when(bloodGroupService).getBloodGroupById(anyLong());
        doReturn(okResponse()).when(bloodGroupService).autocomplete(anyString());
        doReturn(okResponse()).when(bloodGroupService).bloodGroupDropDown();
        doReturn(okResponse()).when(bloodGroupService).bloodGroupList(any());
        doReturn(okResponse()).when(bloodGroupService).deleteBloodGroup(anyLong());

        mockMvc.perform(post("/bloodGroup/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"bloodGroupCode\":\"A+\",\"bloodGroupName\":\"A+\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/bloodGroup/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"bloodGroupCode\":\"A+\",\"bloodGroupName\":\"A+\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bloodGroup/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bloodGroup/autocomplete").param("searchString", "A"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bloodGroup/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/bloodGroup/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"A\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/bloodGroup/delete/1"))
                .andExpect(status().isOk());
    }
}
