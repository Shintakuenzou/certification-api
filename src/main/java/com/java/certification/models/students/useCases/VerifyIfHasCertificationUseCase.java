package com.java.certification.models.students.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.certification.models.students.dto.VerifyHasCertificationDTO;
import com.java.certification.models.students.repository.CertificationStudentRepository;

@Service // Significa que essa camada será uma camada de serviço
public class VerifyIfHasCertificationUseCase {

    @Autowired
    private CertificationStudentRepository certificationStudentEntityRepository;

    public boolean execute(VerifyHasCertificationDTO dto) {
        // validação
        var result = this.certificationStudentEntityRepository.findByStudentEmailAndTechnology(dto.getEmail(),
                dto.getTechnology());
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }
}
