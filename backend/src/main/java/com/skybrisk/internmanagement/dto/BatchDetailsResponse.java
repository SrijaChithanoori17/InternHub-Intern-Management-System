
        package com.skybrisk.internmanagement.dto;

import java.time.LocalDate;
import java.util.List;

public class BatchDetailsResponse {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalInterns;
    private List<InternResponse> interns;

    public BatchDetailsResponse() {
    }

    public BatchDetailsResponse(
            Long id,
            LocalDate startDate,
            LocalDate endDate,
            Integer totalInterns,
            List<InternResponse> interns
    ) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalInterns = totalInterns;
        this.interns = interns;
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

    public List<InternResponse> getInterns() {
        return interns;
    }

    public void setInterns(List<InternResponse> interns) {
        this.interns = interns;
    }
}

