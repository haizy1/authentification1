package com.management.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AnnonceDto {

    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;

    public AnnonceDto(Long id, String titre, String description, LocalDateTime dateCreation) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
