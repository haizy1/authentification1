package com.management.service;

import com.management.dto.AnnonceDto;
import com.management.entity.Annonce;
import com.management.repository.AnnonceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    // Méthode pour créer une annonce
    public AnnonceDto createAnnonce(String titre, String description, Long userId) {
        Annonce annonce = Annonce.builder()
                .titre(titre)
                .description(description)
                .dateCreation(null) // La date de création sera définie automatiquement dans l'entité
                .build();
        annonce = annonceRepository.save(annonce);

        // Vous pouvez ici mapper l'entité Annonce en AnnonceDto si nécessaire
        return new AnnonceDto(annonce.getId(), annonce.getTitre(), annonce.getDescription(), annonce.getDateCreation());
    }

    // Méthode pour obtenir toutes les annonces
    public List<AnnonceDto> getAllAnnonces() {
        List<Annonce> annonces = annonceRepository.findAll();
        return annonces.stream()
                .map(annonce -> new AnnonceDto(annonce.getId(), annonce.getTitre(), annonce.getDescription(), annonce.getDateCreation()))
                .collect(Collectors.toList());
    }
}

