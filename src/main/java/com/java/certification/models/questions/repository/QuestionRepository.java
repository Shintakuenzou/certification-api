package com.java.certification.models.questions.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.certification.models.questions.entities.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {
    // Listar todas as questões das tecnologias que o usuario for fazer a prova
    // Automaticamente por baixo dos panos ele vai mapear o atributo da
    // QuestionEntity
    List<QuestionEntity> findByTechnology(String technology);
}
