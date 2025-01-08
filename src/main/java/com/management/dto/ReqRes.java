package com.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.entity.OurUsers;
import com.management.enums.Genre;
import com.management.enums.Role;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int id;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String nom;
    private String prenom;
    private String motdepasse;
    private String adresse;
    private String numtele;
    private Role role;
    private Genre genre;
    private String email;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;

}
