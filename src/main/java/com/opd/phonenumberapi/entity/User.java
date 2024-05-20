package com.opd.phonenumberapi.entity;

import com.opd.phonenumberapi.StringCryptConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.stereotype.Indexed;

@Entity()
@Table(name = "users", indexes = @Index(name="username_index",columnList="username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;

    @Column()
    @NotBlank
    private String firstName;

    @Column
    @NotBlank
    private String lastName;

    @Column
    private String fatherName;

    @Column
    @Pattern(regexp = "^((8|\\+\\d)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    @Convert(converter= StringCryptConverter.class)
    private String workPhoneNumber;

    @Column
    @Pattern(regexp = "^((8|\\+\\d)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    @Convert(converter= StringCryptConverter.class)
    private String mobilePhoneNumber;

    @Column
    private String directorate;

    @Column
    private String department;

    @Column
    private String unit;

    @Column
    private String service;

    @Column
    private String jobTitle;


    public User(String firstName, String lastName, String fatherName, String workPhoneNumber, String mobilePhoneNumber,
                String directorate, String department, String unit, String service, String jobTitle, Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.workPhoneNumber = workPhoneNumber;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.directorate = directorate;
        this.department = department;
        this.unit = unit;
        this.service =service;
        this.jobTitle = jobTitle;
        this.id = id;
    }

    public User() {
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getDirectorate() {
        return directorate;
    }

    public void setDirectorate(String directorate) {
        this.directorate = directorate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
