package com.java.certification.models.questions.controller;

import java.util.List;
 
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.certification.models.questions.dto.AlternativesResultDTO;
import com.java.certification.models.questions.dto.QuestionResultDTO;
import com.java.certification.models.questions.entities.AlternativeEntity;
import com.java.certification.models.questions.entities.QuestionEntity;
import com.java.certification.models.questions.repository.QuestionRepository;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology) {
        var result = this.questionRepository.findByTechnology(technology);
        // Stream faz com que tenha acesso a filter e map e etc
        var toMap = result.stream().map(question -> mapQuestionDTO(question)).collect(Collectors.toList());
        return toMap;
    }

    static QuestionResultDTO mapQuestionDTO(QuestionEntity question) {
        var questionResultDTO = QuestionResultDTO.builder()
                .id(question.getId())
                .technology(question.getTechnology())
                .description(question.getDescription()).build();

        List<AlternativesResultDTO> alternativesResultDTOs = question
                .getAlternatives().stream()
                .map(alternative -> mapAlternativeDTO(alternative))
                .collect(Collectors.toList());

        questionResultDTO.setAlternativesResultDTOs(alternativesResultDTOs);

        return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeDTO(AlternativeEntity alternativesResultDTO) {
        return AlternativesResultDTO.builder()
                .id(alternativesResultDTO
                        .getId())
                .description(alternativesResultDTO.getDescription())
                .build();
    }
}
