package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequestDto {
    private Long id;

    @NotBlank(message = "City code can not be blank")
    private String cityCode;

    @NotBlank(message = "City name can not be blank")
    private String cityName;

    @NotNull(message = "Taluka id can not be null")
    private Long talukaId;

    private Boolean active = true;
}
