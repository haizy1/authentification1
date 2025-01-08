package com.management.controller;

import com.management.dto.AnnonceDto;
import com.management.dto.ReqRes;
import com.management.entity.OurUsers;
import com.management.service.AnnonceService;
import com.management.service.TokenService;
import com.management.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserManagementController {
    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AnnonceService annonceService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        OurUsers user = usersManagementService.findByVerificationToken(token);

        if (user == null) {
            return ResponseEntity.status(400).body("Token invalide.");
        }

        if (user.isVerified()) {
            return ResponseEntity.ok("Utilisateur déjà vérifié.");
        }

        // Mettre à jour et sauvegarder l'utilisateur
        user.setVerified(true);
        usersManagementService.save(user);

        return ResponseEntity.ok("Compte vérifié avec succès !");
    }

    @PostMapping("/auth/registeretudiant")
    public ResponseEntity<ReqRes> registeretudiant(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.registeretudiant(reg));
    }

    @PostMapping("/auth/registerloueur")
    public ResponseEntity<ReqRes> registerloueur(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.registerloueur(reg));
    }

    @PostMapping("/auth/registeradmin")
    public ResponseEntity<ReqRes> registeradmin(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.registeradmin(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqres){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }



    //todo hadi dyali
    @GetMapping("/auth/user-details")
    public ResponseEntity<ReqRes> getUserDetails(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        ReqRes userDetails = usersManagementService.getUserDetails(token);
        if (userDetails.getStatusCode() == 200) {
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userDetails);
        }
    }


}