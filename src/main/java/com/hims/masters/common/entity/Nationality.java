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
@Table(name = "mt_nationality")
public class Nationality extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nationality code can not be blank")
    @Column(name = "nationality_code", unique = true)
    private String nationalityCode;

    @NotBlank(message = "Nationality name can not be blank")
    @Column(name = "nationality_name", length = 100, unique = true)
    private String nationalityName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
