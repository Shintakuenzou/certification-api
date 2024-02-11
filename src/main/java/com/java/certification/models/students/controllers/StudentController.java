package com.java.certification.models.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.certification.models.students.dto.StudentCertificationAnswerDTO;
import com.java.certification.models.students.dto.VerifyHasCertificationDTO;
import com.java.certification.models.students.useCases.StudentCertificationAnswersUseCase;
import com.java.certification.models.students.useCases.VerifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {

    // Preciso usar o UseCase
    @Autowired // instancia automaticamente.
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(
            @RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {
        // Email
        // Tech
        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if (result) {
            return "Usuário já fez a prova";
        }
        System.out.println(verifyHasCertificationDTO);
        return "Usuário pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswer(
            @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
        try {
            var result = studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
            return ResponseEntity.ok().body(result);

        } catch (Exception err) {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
}
