package com.java.certification.models.students.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.certification.models.students.entities.StudentsEntity;

public interface StudentRepository extends JpaRepository<StudentsEntity, UUID> {

    public Optional<StudentsEntity> findByEmail(String email);

}