package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaRequestDto {
    private Long id;

    @NotBlank(message = "Area code can not be blank")
    private String areaCode;

    @NotBlank(message = "Area name can not be blank")
    private String areaName;

    @NotNull(message = "Pincode id can not be null")
    private Long pinCodeId;

    private Boolean active = true;
}
