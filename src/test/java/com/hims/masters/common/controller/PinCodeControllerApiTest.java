package com.hims.masters.common.controller;

import com.hims.masters.common.services.PinCodeService;
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

@WebMvcTest(PinCodeController.class)
@Import(ApiResponseFactory.class)
class PinCodeControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PinCodeService pinCodeService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllPinCodeApis() throws Exception {
        doReturn(okResponse()).when(pinCodeService).savePinCode(any());
        doReturn(okResponse()).when(pinCodeService).updatePinCode(any());
        doReturn(okResponse()).when(pinCodeService).getPinCodeById(anyLong());
        doReturn(okResponse()).when(pinCodeService).autocomplete(anyString());
        doReturn(okResponse()).when(pinCodeService).pinCodeDropDown();
        doReturn(okResponse()).when(pinCodeService).pinCodeList(any());
        doReturn(okResponse()).when(pinCodeService).deletePinCode(anyLong());

        mockMvc.perform(post("/pincode/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"pincode\":\"411001\",\"cityId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/pincode/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"pincode\":\"411001\",\"cityId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pincode/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pincode/autocomplete").param("searchString", "411"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pincode/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/pincode/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"411\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/pincode/delete/1"))
                .andExpect(status().isOk());
    }
}
