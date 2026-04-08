package com.hims.masters.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCategoryRequestDto {
    private Long id;

    @NotBlank(message = "Category can not be blank")
    private String category;

    @NotBlank(message = "Code can not be blank")
    private String code;

    private Long employeeTypeId;
    private Boolean active = true;
}
