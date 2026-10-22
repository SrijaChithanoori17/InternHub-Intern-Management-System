
        package com.skybrisk.internmanagement.service;

import com.skybrisk.internmanagement.dto.BatchResponse;
import com.skybrisk.internmanagement.dto.CreateBatchRequest;
import com.skybrisk.internmanagement.entity.Batch;
import com.skybrisk.internmanagement.repository.BatchRepository;
import org.springframework.stereotype.Service;
import com.skybrisk.internmanagement.repository.InternRepository;
import com.skybrisk.internmanagement.dto.BatchDetailsResponse;
import com.skybrisk.internmanagement.dto.InternResponse;
import com.skybrisk.internmanagement.exception.ResourceNotFoundException;

        @Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final InternRepository internRepository;

            public BatchService(
                    BatchRepository batchRepository,
                    InternRepository internRepository
            ) {
                this.batchRepository = batchRepository;
                this.internRepository = internRepository;
            }

    public BatchResponse createBatch(CreateBatchRequest request) {

        Batch batch = new Batch();

        batch.setStartDate(request.getStartDate());

        // Automatically calculate end date
        batch.setEndDate(
                request.getStartDate().plusMonths(6)
        );

        // First intern in this batch gets sequence 001
        batch.setNextSequence(1);

        Batch savedBatch = batchRepository.save(batch);

        return new BatchResponse(
                savedBatch.getId(),
                savedBatch.getStartDate(),
                savedBatch.getEndDate(),
                0
        );
    }

    public java.util.List<BatchResponse> getAllBatches() {

        return batchRepository.findAll()
                .stream()
                .map(batch -> {

                    long totalInterns =
                            internRepository.countByBatchId(batch.getId());

                    return new BatchResponse(
                            batch.getId(),
                            batch.getStartDate(),
                            batch.getEndDate(),
                            (int) totalInterns
                    );
                })
                .toList();
    }


            public BatchDetailsResponse getBatchDetails(Long batchId) {

                // 1. Find the batch
                Batch batch = batchRepository.findById(batchId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Batch not found")
                        );

                // 2. Find all interns assigned to this batch
                java.util.List<InternResponse> interns =
                        internRepository.findByBatchId(batchId)
                                .stream()
                                .map(intern -> {

                                    return new InternResponse(
                                            intern.getId(),
                                            intern.getInternId(),
                                            intern.getName(),
                                            intern.getEmail(),
                                            intern.getMobileNumber(),
                                            intern.getIdCardType(),
                                            intern.getDateOfJoining(),
                                            batch.getId(),
                                            batch.getStartDate(),
                                            batch.getEndDate()
                                    );
                                })
                                .toList();

                // 3. Return batch + intern details
                return new BatchDetailsResponse(
                        batch.getId(),
                        batch.getStartDate(),
                        batch.getEndDate(),
                        interns.size(),
                        interns
                );
            }


}

