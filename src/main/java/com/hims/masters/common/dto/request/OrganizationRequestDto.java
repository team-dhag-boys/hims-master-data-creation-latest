package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequestDto {
    private Long id;

    @NotBlank(message = "Organization code can not be blank")
    private String organizationCode;

    @NotBlank(message = "Organization name can not be blank")
    private String organizationName;

    private Boolean active = true;
}
