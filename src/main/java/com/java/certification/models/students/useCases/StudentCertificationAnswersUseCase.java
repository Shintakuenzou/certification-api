package com.java.certification.models.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.certification.models.questions.entities.QuestionEntity;
import com.java.certification.models.questions.repository.QuestionRepository;
import com.java.certification.models.students.dto.StudentCertificationAnswerDTO;
import com.java.certification.models.students.dto.VerifyHasCertificationDTO;
import com.java.certification.models.students.entities.AnswersCertificationEntity;
import com.java.certification.models.students.entities.CertificationStudentEntity;
import com.java.certification.models.students.entities.StudentsEntity;
import com.java.certification.models.students.repository.CertificationStudentRepository;
import com.java.certification.models.students.repository.StudentRepository;

import lombok.Builder;

@Service
@Builder
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    // throws Exception: Ao colcoar isso podemos falar ao Java que quem chamar esse
    // método trate as exeções
    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase
                .execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Voce ja tirou sua certificação.");
        }

        // Buscar as alternativas certas das perguntas
        List<QuestionEntity> questionEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationEntity> answersCertifications = new ArrayList<>();
        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
            var question = questionEntity.stream()
                    .filter((q) -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();

            var findCorrectAlternative = question.getAlternatives().stream()
                    .filter(alternative -> alternative.isCorrect()).findFirst().get();

            if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                questionAnswer.setIsCorrect(true);
                correctAnswers.incrementAndGet();
            } else {
                questionAnswer.setIsCorrect(false);
            }

            var answersCertificationEntity = AnswersCertificationEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestionID()).isCorrect(questionAnswer.getIsCorrect()).build();
            answersCertifications.add(answersCertificationEntity);

        });

        // Verificar se existe o estudante.
        var student = studentRepository.findByEmail(dto.getEmail());

        UUID studentID;
        if (student.isEmpty()) {
            var studentCreated = StudentsEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                .grade(correctAnswers.get())
                .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answerCertification -> {
            answerCertification.setCertificationID(certificationStudentEntity.getId());
            answerCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationEntities(answersCertifications);

        return certificationStudentCreated;
        // Salvar as informações da certificação
    }
}