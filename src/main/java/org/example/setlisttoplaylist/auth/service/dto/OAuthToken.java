package org.example.setlisttoplaylist.auth.service.dto;

public interface OAuthToken {

    String accessToken();

    String refreshToken();

    String tokenType();

    long expiresIn();

}