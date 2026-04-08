package com.hims.masters.employee.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "mt_designation")
public class Designation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    @NotBlank(message = "Designation can not be blank")
    @Column(name = "designation", length = 100, unique = true)
    private String designation;

    @NotBlank(message = "Code can not be blank")
    @Column(name = "code", length = 100, unique = true)
    private String code;

    private Boolean deleteFlag = false;
}
