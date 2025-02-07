package com.wsystems.residentstore.dto;

public record LoginResponse(String accessToken, Long expiresIn) {

}

//Classe da responta do endpoint de "Login", que irá retornar um token (accessToken) e o tempo da sessão (expiresIn).