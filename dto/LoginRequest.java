package com.wsystems.residentstore.dto;

public record LoginRequest(String email, String password) {
}

//Classe de requisição que iremos passar o usuário (email), e a senha (password).