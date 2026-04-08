package com.hims.masters.common.dto.request;


import com.hims.masters.common.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrefixRequestDto {

    @NotBlank(message = "Prefix code can not be blank")
    private String prefixCode;

    @NotBlank(message = "Prefix Name can not be blank")
    private String prefixName;

    private Gender genderPrefix;

    private Boolean active = true;

    private Long id;
}
