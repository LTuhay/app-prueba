package app.controllers;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dto.LoginResponseDTO;
import app.dto.UserLoginDTO;
import app.entities.User;
import app.services.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final int EXPIRATION_TIME_IN_MIN = 60;

    @Autowired
    private IUserService userService;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO credentials) {
        try {
            Optional<User> userOptional = userService.findByUsername(credentials.getUsername());
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }
            User user = userOptional.get();
            if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
                String token = Jwts.builder()
                        .setSubject(credentials.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MIN * 60 * 1000))
                        .signWith(secretKey, SignatureAlgorithm.HS256)
                        .compact();

                LoginResponseDTO response = new LoginResponseDTO(token, user.getUsername());
                System.out.println(response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
