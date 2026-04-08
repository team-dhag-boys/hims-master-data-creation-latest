package com.hims.masters.employee.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mt_employee_benefit_type")
public class EmployeeBenefitType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Benefit type code can not be blank")
    @Column(name = "benefit_type_code", length = 100, unique = true)
    private String benefitTypeCode;

    @NotBlank(message = "Benefit type name can not be blank")
    @Column(name = "benefit_type_name", length = 100, unique = true)
    private String benefitTypeName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
