
        package com.skybrisk.internmanagement.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateBatchRequest {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    public CreateBatchRequest() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}

