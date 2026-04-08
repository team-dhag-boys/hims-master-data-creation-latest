package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentUnitMappingRequestDto {
    private Long id;

    @NotNull(message = "Department id can not be null")
    private Long departmentId;

    @NotNull(message = "Unit id can not be null")
    private Long unitId;

    private Boolean active = true;
}
