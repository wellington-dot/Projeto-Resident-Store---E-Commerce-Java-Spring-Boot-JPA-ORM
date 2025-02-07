package com.wsystems.residentstore.controllers;

import com.wsystems.residentstore.dto.LoginRequest;
import com.wsystems.residentstore.dto.LoginResponse;
import com.wsystems.residentstore.repositories.UserRespository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRespository repository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRespository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var user = repository.findByEmail(loginRequest.email());

        //Condição para verificar se o login está correto
        //user.isEmpty - verificando se o usuário existe no Bd, ou se retornou vazio
        //!user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder - chamando metodo na classe de usuário, passando a senha e a classe do Bcrypt para comparar senhas.
        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)){
            throw new BadCredentialsException("Usuário o senha inválidos!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUsername().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}
