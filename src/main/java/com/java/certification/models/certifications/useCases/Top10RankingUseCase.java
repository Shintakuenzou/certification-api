package com.java.certification.models.certifications.useCases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.certification.models.students.entities.CertificationStudentEntity;
import com.java.certification.models.students.repository.CertificationStudentRepository;

@Service
public class Top10RankingUseCase {

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    public List<CertificationStudentEntity> execute() {
        return this.certificationStudentRepository.findTio10ByOrderGradeDesc();
    }
}
