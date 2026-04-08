package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitRequestDto {
    private Long id;

    @NotBlank(message = "Unit code can not be blank")
    private String unitCode;

    @NotBlank(message = "Unit name can not be blank")
    private String unitName;

    @NotNull(message = "Organization id can not be null")
    private Long organizationId;

    private Boolean active = true;
}
