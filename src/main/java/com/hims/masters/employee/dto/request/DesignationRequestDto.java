package com.hims.masters.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignationRequestDto {
    private Long id;

    @NotBlank(message = "Designation can not be blank")
    private String designation;

    @NotBlank(message = "Code can not be blank")
    private String code;

    private Boolean active = true;
}
