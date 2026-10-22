
        package com.skybrisk.internmanagement.controller;

import com.skybrisk.internmanagement.dto.CreateInternRequest;
import com.skybrisk.internmanagement.dto.InternResponse;
import com.skybrisk.internmanagement.entity.IdCardType;
import com.skybrisk.internmanagement.service.InternService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skybrisk.internmanagement.dto.UpdateInternRequest;

import java.util.List;

@RestController
@RequestMapping("/api/interns")
public class InternController {

    private final InternService internService;

    public InternController(InternService internService) {
        this.internService = internService;
    }

    // =========================
    // Create Intern
    // =========================
    @PostMapping
    public ResponseEntity<InternResponse> createIntern(
            @Valid @RequestBody CreateInternRequest request
    ) {

        InternResponse response =
                internService.createIntern(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // Get / Search / Filter Interns
    // =========================
    @GetMapping
    public ResponseEntity<List<InternResponse>> getInterns(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            Long batchId,

            @RequestParam(required = false)
            IdCardType idCardType
    ) {

        // No filters → return all interns
        if (name == null &&
                batchId == null &&
                idCardType == null) {

            return ResponseEntity.ok(
                    internService.getAllInterns()
            );
        }

        // At least one filter → search/filter
        return ResponseEntity.ok(
                internService.searchInterns(
                        name,
                        batchId,
                        idCardType
                )
        );
    }

// =========================
// Update Intern
// =========================
    @PutMapping("/{id}")
    public ResponseEntity<InternResponse> updateIntern(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInternRequest request
    ) {

        InternResponse response =
                internService.updateIntern(id, request);

        return ResponseEntity.ok(response);
    }


// =========================
// Delete Intern
// =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntern(
            @PathVariable Long id
    ) {

        internService.deleteIntern(id);

        return ResponseEntity.noContent().build();
    }


}

