
package com.skybrisk.internmanagement.dto;

import com.skybrisk.internmanagement.entity.IdCardType;
import java.time.LocalDate;

public class InternResponse {

    private Long id;
    private String internId;
    private String name;
    private String email;
    private String mobileNumber;
    private IdCardType idCardType;
    private LocalDate dateOfJoining;
    private Long batchId;
    private LocalDate batchStartDate;
    private LocalDate batchEndDate;

    public InternResponse() {
    }

    public InternResponse(
            Long id,
            String internId,
            String name,
            String email,
            String mobileNumber,
            IdCardType idCardType,
            LocalDate dateOfJoining,
            Long batchId,
            LocalDate batchStartDate,
            LocalDate batchEndDate
    ) {
        this.id = id;
        this.internId = internId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.idCardType = idCardType;
        this.dateOfJoining = dateOfJoining;
        this.batchId = batchId;
        this.batchStartDate = batchStartDate;
        this.batchEndDate = batchEndDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternId() {
        return internId;
    }

    public void setInternId(String internId) {
        this.internId = internId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public IdCardType getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(IdCardType idCardType) {
        this.idCardType = idCardType;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDate getBatchStartDate() {
        return batchStartDate;
    }

    public void setBatchStartDate(LocalDate batchStartDate) {
        this.batchStartDate = batchStartDate;
    }

    public LocalDate getBatchEndDate() {
        return batchEndDate;
    }

    public void setBatchEndDate(LocalDate batchEndDate) {
        this.batchEndDate = batchEndDate;
    }
}
