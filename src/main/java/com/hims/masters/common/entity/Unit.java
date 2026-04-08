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
@Table(name = "mt_unit")
public class Unit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Unit code can not be blank")
    @Column(name = "unit_code", length = 100, unique = true)
    private String unitCode;

    @NotBlank(message = "Unit name can not be blank")
    @Column(name = "unit_name", length = 100, unique = true)
    private String unitName;

    @NotNull(message = "Organization id can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
