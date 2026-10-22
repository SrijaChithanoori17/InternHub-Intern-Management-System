
        package com.skybrisk.internmanagement.controller;

import com.skybrisk.internmanagement.dto.BatchResponse;
import com.skybrisk.internmanagement.dto.CreateBatchRequest;
import com.skybrisk.internmanagement.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.skybrisk.internmanagement.dto.BatchDetailsResponse;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(
            @Valid @RequestBody CreateBatchRequest request
    ) {

        BatchResponse response = batchService.createBatch(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

// =========================
// Get All Batches
// =========================
    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAllBatches() {

        List<BatchResponse> batches =
                batchService.getAllBatches();

        return ResponseEntity.ok(batches);
    }


// =========================
// Get Batch Details
// =========================
    @GetMapping("/{id}")
    public ResponseEntity<BatchDetailsResponse> getBatchDetails(
            @PathVariable Long id
    ) {

        BatchDetailsResponse response =
                batchService.getBatchDetails(id);

        return ResponseEntity.ok(response);
    }


}

