
        package com.skybrisk.internmanagement.service;

import com.skybrisk.internmanagement.dto.CreateInternRequest;
import com.skybrisk.internmanagement.dto.InternResponse;
import com.skybrisk.internmanagement.entity.Batch;
import com.skybrisk.internmanagement.entity.IdCardType;
import com.skybrisk.internmanagement.entity.Intern;
import com.skybrisk.internmanagement.repository.BatchRepository;
import com.skybrisk.internmanagement.repository.InternRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.skybrisk.internmanagement.dto.UpdateInternRequest;
import com.skybrisk.internmanagement.exception.InvalidRequestException;
import com.skybrisk.internmanagement.exception.ResourceNotFoundException;
import com.skybrisk.internmanagement.exception.DuplicateResourceException;

import java.time.format.DateTimeFormatter;

@Service
public class InternService {

    private final InternRepository internRepository;
    private final BatchRepository batchRepository;

    private static final DateTimeFormatter ID_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public InternService(
            InternRepository internRepository,
            BatchRepository batchRepository
    ) {
        this.internRepository = internRepository;
        this.batchRepository = batchRepository;
    }

    @Transactional
    public InternResponse createIntern(CreateInternRequest request) {

        // 1. Find and lock the selected batch
        Batch batch = batchRepository
                .findByIdForUpdate(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

// 2. Validate joining date against batch period
        if (request.getDateOfJoining().isBefore(batch.getStartDate())
                || request.getDateOfJoining().isAfter(batch.getEndDate())) {
            throw new InvalidRequestException(
                    "Date of joining must be within the batch period"
            );
        }


        if (internRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        // 3. Get the current sequence number
        Integer sequence = batch.getNextSequence();

        // 4. Determine the ID prefix
        String prefix;

        if (request.getIdCardType() == IdCardType.PREMIUM) {
            prefix = "EMP";
        } else {
            prefix = "TDA";
        }

        // 5. Format joining date as yyyyMMdd
        String joiningDate =
                request.getDateOfJoining()
                        .format(ID_DATE_FORMAT);

        // 6. Format sequence as three digits
        String sequenceNumber =
                String.format("%03d", sequence);

        // 7. Generate Intern ID
        String internId =
                prefix + joiningDate + "-" + sequenceNumber;

        // 8. Create Intern entity
        Intern intern = new Intern();

        intern.setInternId(internId);
        intern.setName(request.getName());
        intern.setEmail(request.getEmail());
        intern.setMobileNumber(request.getMobileNumber());
        intern.setIdCardType(request.getIdCardType());
        intern.setDateOfJoining(request.getDateOfJoining());
        intern.setBatch(batch);

        // 9. Increment sequence for the next intern
        batch.setNextSequence(sequence + 1);

        // 10. Save batch and intern
        batchRepository.save(batch);

        Intern savedIntern = internRepository.save(intern);

        // 11. Return response DTO
        return new InternResponse(
                savedIntern.getId(),
                savedIntern.getInternId(),
                savedIntern.getName(),
                savedIntern.getEmail(),
                savedIntern.getMobileNumber(),
                savedIntern.getIdCardType(),
                savedIntern.getDateOfJoining(),
                batch.getId(),
                batch.getStartDate(),
                batch.getEndDate()
        );
    }

    public java.util.List<InternResponse> getAllInterns() {

        return internRepository.findAll()
                .stream()
                .map(intern -> {

                    Batch batch = intern.getBatch();

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
    }


    public java.util.List<InternResponse> searchInterns(
            String name,
            Long batchId,
            IdCardType idCardType
    ) {

        return internRepository
                .searchInterns(name, batchId, idCardType)
                .stream()
                .map(intern -> {

                    Batch batch = intern.getBatch();

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
    }


    public InternResponse updateIntern(
            Long id,
            UpdateInternRequest request
    ) {

        // 1. Find the intern
        Intern intern = internRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intern not found"));

        // 2. Check if the new email belongs to another intern
        internRepository.findByEmail(request.getEmail())
                .ifPresent(existingIntern -> {

                    if (!existingIntern.getId().equals(id)) {
                        throw new DuplicateResourceException("Email already exists");
                    }
                });

        // 3. Update only editable fields
        intern.setName(request.getName());
        intern.setEmail(request.getEmail());
        intern.setMobileNumber(request.getMobileNumber());

        // 4. Save changes
        Intern updatedIntern = internRepository.save(intern);

        // 5. Get batch information
        Batch batch = updatedIntern.getBatch();

        // 6. Return updated intern
        return new InternResponse(
                updatedIntern.getId(),
                updatedIntern.getInternId(),
                updatedIntern.getName(),
                updatedIntern.getEmail(),
                updatedIntern.getMobileNumber(),
                updatedIntern.getIdCardType(),
                updatedIntern.getDateOfJoining(),
                batch.getId(),
                batch.getStartDate(),
                batch.getEndDate()
        );
    }


    public void deleteIntern(Long id) {
        if (!internRepository.existsById(id)) {
            throw new ResourceNotFoundException("Intern not found");
        }

        internRepository.deleteById(id);
    }


}

