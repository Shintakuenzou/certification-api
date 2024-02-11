package com.java.certification.models.students.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.certification.models.students.entities.CertificationStudentEntity;

@Repository
public interface CertificationStudentRepository extends JpaRepository<CertificationStudentEntity, UUID> {

    // Customizar a Query
    @Query("SELECT c FROM certifications c INNER JOIN c.studentsEntity std WHERE std.email = :email AND c.technology = :technology")
    List<CertificationStudentEntity> findByStudentEmailAndTechnology(String email, String technology);

    @Query("SELECT c FROM certifications c ORDER BY c.grade DESC LIMIT 10")
    List<CertificationStudentEntity> findTio10ByOrderGradeDesc();
}
