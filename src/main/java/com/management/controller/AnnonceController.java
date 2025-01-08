package com.management.controller;

import com.management.dto.AnnonceDto;
import com.management.service.AnnonceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnnonceController {

    private final AnnonceService annonceService;

    // Endpoint pour créer une annonce (accessible sans authentification)
    @PostMapping("/auth/createannonce")
    public ResponseEntity<AnnonceDto> createAnnonce(
            @RequestParam String titre,
            @RequestParam String description,
            @RequestParam Long userId) {
        // Logique pour créer une annonce
        AnnonceDto annonce = annonceService.createAnnonce(titre, description, userId);
        return ResponseEntity.ok(annonce);
    }

    // Endpoint pour obtenir toutes les annonces (accessible uniquement pour l'administrateur)
    @GetMapping("/admin/get-all-annonces")
    public ResponseEntity<List<AnnonceDto>> getAllAnnonces() {
        List<AnnonceDto> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }
}