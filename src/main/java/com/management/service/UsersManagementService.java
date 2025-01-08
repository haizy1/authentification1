package com.management.service;

import com.management.config.JWTAuthFilter;
import com.management.dto.ReqRes;
import com.management.entity.OurUsers;
import com.management.repository.AnnonceRepository;
import com.management.dto.AnnonceDto;
import com.management.repository.UsersRepo;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersManagementService {


    public OurUsers findByVerificationToken(String token) {
        return usersRepo.findByVerificationToken(token); // Utilise la méthode du repository pour trouver l'utilisateur
    }

    public void save(OurUsers user) {
        usersRepo.save(user); // Sauvegarde les modifications
    }


    @Autowired
    private AnnonceRepository annonceRepository;


    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    public ReqRes registeretudiant(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            // Vérifier si l'email existe déjà
            if (usersRepo.existsByEmail(registrationRequest.getEmail())) {
                resp.setStatusCode(400);
                resp.setMessage("Email already in use.");
                return resp;
            }

            // Créer un nouvel utilisateur
            OurUsers ourUser = new OurUsers();
            ourUser.setNom(registrationRequest.getNom());
            ourUser.setPrenom(registrationRequest.getPrenom());
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setMotdepasse(passwordEncoder.encode(registrationRequest.getMotdepasse()));
            ourUser.setRole(registrationRequest.getRole());

            // Générer un token de vérification
            String token = UUID.randomUUID().toString();
            ourUser.setVerificationToken(token);
            ourUser.setVerified(false);

            OurUsers ourUsersResult = usersRepo.save(ourUser);

            if (ourUsersResult.getId() > 0) {
                // Envoyer l'e-mail
                String link = "http://localhost:3003/verify?token=" + token;
                String body = "<p>Bienvenue ! Merci de vous être inscrit en tant qu'étudiant. Cliquez sur le lien ci-dessous pour confirmer votre compte :</p>" +
                        "<a href=\"" + link + "\">Confirmer mon compte</a>";

                try {
                    emailService.sendEmail(ourUser.getEmail(), "Confirmation de votre inscription", body);
                } catch (Exception e) {
                    resp.setStatusCode(500);
                    resp.setMessage("Erreur lors de l'envoi de l'e-mail.");
                    resp.setError(e.getMessage());
                    return resp;
                }

                resp.setOurUsers(ourUsersResult);
                resp.setMessage("Étudiant enregistré avec succès. Vérifiez votre e-mail pour confirmer.");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }


    public ReqRes registerloueur(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            // Vérifier si l'email existe déjà
            if (usersRepo.existsByEmail(registrationRequest.getEmail())) {
                resp.setStatusCode(400);
                resp.setMessage("Email already in use.");
                return resp;
            }

            // Créer un nouvel utilisateur
            OurUsers ourUser = new OurUsers();
            ourUser.setNom(registrationRequest.getNom());
            ourUser.setPrenom(registrationRequest.getPrenom());
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setGenre(registrationRequest.getGenre());
            ourUser.setAdresse(registrationRequest.getAdresse());
            ourUser.setNumtele(registrationRequest.getNumtele());
            ourUser.setMotdepasse(passwordEncoder.encode(registrationRequest.getMotdepasse()));
            ourUser.setRole(registrationRequest.getRole());

            // Générer un token de vérification
            String token = UUID.randomUUID().toString();
            ourUser.setVerificationToken(token);
            ourUser.setVerified(false);

            OurUsers ourUsersResult = usersRepo.save(ourUser);

            if (ourUsersResult.getId() > 0) {
                // Envoyer l'e-mail
                String link = "http://localhost:3003/verify?token=" + token;
                String body = "<p>Bienvenue ! Merci de vous être inscrit en tant que loueur. Cliquez sur le lien ci-dessous pour confirmer votre compte :</p>" +
                        "<a href=\"" + link + "\">Confirmer mon compte</a>";

                try {
                    emailService.sendEmail(ourUser.getEmail(), "Confirmation de votre inscription", body);
                } catch (Exception e) {
                    resp.setStatusCode(500);
                    resp.setMessage("Erreur lors de l'envoi de l'e-mail.");
                    resp.setError(e.getMessage());
                    return resp;
                }

                resp.setOurUsers(ourUsersResult);
                resp.setMessage("Loueur enregistré avec succès. Vérifiez votre e-mail pour confirmer.");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }


    public ReqRes registeradmin(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setMotdepasse(passwordEncoder.encode(registrationRequest.getMotdepasse()));
            ourUser.setRole(registrationRequest.getRole());
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId() > 0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getMotdepasse()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setNom(updatedUser.getNom());
                existingUser.setPrenom(updatedUser.getPrenom());
                existingUser.setAdresse(updatedUser.getAdresse());
                existingUser.setNumtele(updatedUser.getNumtele());
                existingUser.setGenre(updatedUser.getGenre());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setMotdepasse(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }


    public ReqRes getUserDetails(String token) {
        ReqRes response = new ReqRes();
        try {
            // Extract the email from the JWT token
            String email = jwtUtils.extractUsername(token);

            // Fetch user details using the email
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                OurUsers user = userOptional.get();

                // Prepare the response with the required details
                response.setId(user.getId());  // This will now fetch the correct id from the registered user
                response.setNom(user.getNom());
                response.setPrenom(user.getPrenom());
                response.setEmail(user.getEmail());
                response.setStatusCode(200);
                response.setMessage("User details retrieved successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while retrieving user details: " + e.getMessage());
        }
        return response;
    }

}
