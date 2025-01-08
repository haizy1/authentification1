package com.management.repository;


import com.management.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<OurUsers, Integer> {
    // Trouver un utilisateur par son token de vérification
      OurUsers findByVerificationToken(String token);

    // Vérifier si l'email est déjà utilisé
      boolean existsByEmail(String email);

      OurUsers save(OurUsers user);

      Optional<OurUsers> findByEmail(String email);
}
