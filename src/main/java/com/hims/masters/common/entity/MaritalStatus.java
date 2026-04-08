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
@Table(name = "mt_marital_status")
public class MaritalStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Marital status code can not be blank")
    @Column(name = "marital_status_code", length = 10, unique = true)
    private String maritalStatusCode;

    @NotBlank(message = "Marital status name can not be blank")
    @Column(name = "marital_status_name", length = 25, unique = true)
    private String maritalStatusName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
