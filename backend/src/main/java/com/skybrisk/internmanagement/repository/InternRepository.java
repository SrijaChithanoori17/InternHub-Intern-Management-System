
        package com.skybrisk.internmanagement.repository;

import com.skybrisk.internmanagement.entity.IdCardType;
import com.skybrisk.internmanagement.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InternRepository extends JpaRepository<Intern, Long> {

    Optional<Intern> findByEmail(String email);

    List<Intern> findByBatchId(Long batchId);

    List<Intern> findByIdCardType(IdCardType idCardType);

    List<Intern> findByNameContainingIgnoreCase(String name);

    // Search/filter interns by name, batch and ID card type
    @Query("""
            SELECT i
            FROM Intern i
            WHERE (:name IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:batchId IS NULL OR i.batch.id = :batchId)
              AND (:idCardType IS NULL OR i.idCardType = :idCardType)
            """)
    List<Intern> searchInterns(
            @Param("name") String name,
            @Param("batchId") Long batchId,
            @Param("idCardType") IdCardType idCardType
    );

    long countByBatchId(Long batchId);


}

