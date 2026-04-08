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
@Table(name = "mt_employee_category")
public class EmployeeCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    @NotBlank(message = "Category can not be blank")
    @Column(name = "category", length = 50, unique = true)
    private String category;

    @NotBlank(message = "Code can not be blank")
    @Column(name = "code", unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "employee_type_id")
    private EmployeeType employeeType;

    private Boolean deleteFlag = false;

}
