package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDto {
    private Long id;

    @NotBlank(message = "Department code can not be blank")
    private String departmentCode;

    @NotBlank(message = "Department name can not be blank")
    private String departmentName;

    private Boolean active = true;
}
