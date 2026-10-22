
        package com.skybrisk.internmanagement.dto;

import java.time.LocalDate;

public class BatchResponse {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalInterns;

    public BatchResponse() {
    }

    public BatchResponse(
            Long id,
            LocalDate startDate,
            LocalDate endDate,
            Integer totalInterns
    ) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalInterns = totalInterns;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalInterns() {
        return totalInterns;
    }

    public void setTotalInterns(Integer totalInterns) {
        this.totalInterns = totalInterns;
    }
}

