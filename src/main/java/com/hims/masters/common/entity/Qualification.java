package com.hims.masters.common.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mt_qualification")
public class Qualification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Qualification code can not be blank")
    @Column(name = "qualification_code", length = 100, unique = true)
    private String qualificationCode;

    @NotBlank(message = "Qualification name can not be blank")
    @Column(name = "qualification_name", length = 100, unique = true)
    private String qualificationName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
