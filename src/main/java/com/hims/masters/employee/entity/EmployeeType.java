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
@Table(name = "mt_employee_type")
public class EmployeeType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_type", length = 50, unique = true)
    @NotBlank(message = "Employee type can not be blank")
    private String employeeType;

    @NotNull(message = "isClinical can not be null")
    private Boolean isClinical;

    private Boolean applicableToDoctorType=false;

    private Boolean isDoctor = false;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
