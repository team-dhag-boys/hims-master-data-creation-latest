package com.hims.masters.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hims.masters.base.BaseEntity;
import com.hims.masters.common.entity.Gender;
import com.hims.masters.common.entity.Prefix;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "mt_employees")
@JsonIgnoreProperties(value = {
        "createdBy",
        "lastModifiedBy"
})
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee code can not be blank")
    @Column(name = "employee_code", length = 100, unique = true)
    private String employeeCode;

    @ManyToOne
    @JoinColumn(name = "employee_type_id")
    private EmployeeType employeeType;


    @ManyToOne
    @JoinColumn(name = "employee_category_id")
    private EmployeeCategory employeeCategory;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;

    @NotNull(message = "isClinical can not be null")
    private Boolean isClinical;

    @ManyToOne
    @JoinColumn(name = "prefix_id")
    private Prefix prefix;

    @NotBlank(message = "Employee FirstName can not be blank")
    @Column(name = "first_name", length = 50)

    @NotNull
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotBlank(message = "Employee LastName can not be blank")
    @Column(name = "last_name", length = 50)
    @NotNull
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @Column(name = "birthdate", length = 10)
    @NotNull
    private LocalDate birthDate;

    @Email(message = "Email is not valid")
    @Column(name = "email", length = 30)
    private String email;

    @NotNull(message = "mobile number can not be null")
    @Column(name = "mobile", length = 10, unique = true)
    private Long mobileNo;

    @NotNull(message = "address can not be null")
    private String address;


    @Column(name = "experience", length = 10)
    private Short experience;

    @Column(name = "joining_date", length = 10)
//    @NotNull
    private LocalDate joiningDate;


    @Column(name = "registration_no")
    private String employeeRegNo;

    private LocalDate registrationDate;

    private LocalDate resignationDate;

    @Column(name = "aadhar_no", length = 12)
    private Long aadharNo;

    @Column(name = "pan_no", length = 10)
    private String panNo;



}

