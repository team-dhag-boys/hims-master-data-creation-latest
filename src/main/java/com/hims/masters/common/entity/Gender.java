package com.hims.masters.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
@Table(name = "mt_gender")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Gender extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "gender_code", length = 10, unique = true)
    private String genderCode;

    @NotBlank(message = "Gender name can not be blank")
    @Column(name = "gender_name", length = 15, unique = true)
    private String genderName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
