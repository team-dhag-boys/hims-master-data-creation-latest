package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodGroupRequestDto {
    private Long id;

    @NotBlank(message = "Blood group code can not be blank")
    private String bloodGroupCode;

    @NotBlank(message = "Blood group name can not be blank")
    private String bloodGroupName;

    private Boolean active = true;
}
