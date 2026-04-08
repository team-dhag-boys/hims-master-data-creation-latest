package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictRequestDto {
    private Long id;

    @NotBlank(message = "District code can not be blank")
    private String districtCode;

    @NotBlank(message = "District name can not be blank")
    private String districtName;

    @NotNull(message = "State id can not be null")
    private Long stateId;

    private Boolean active = true;
}
