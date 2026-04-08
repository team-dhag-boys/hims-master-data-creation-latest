package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalukaRequestDto {
    private Long id;

    @NotBlank(message = "Taluka code can not be blank")
    private String talukaCode;

    @NotBlank(message = "Taluka name can not be blank")
    private String talukaName;

    @NotNull(message = "District id can not be null")
    private Long districtId;

    private Boolean active = true;
}
