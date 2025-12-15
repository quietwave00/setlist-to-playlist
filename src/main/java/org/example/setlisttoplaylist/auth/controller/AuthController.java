package org.example.setlisttoplaylist.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.AuthService;
import org.example.setlisttoplaylist.auth.service.dto.SessionStatus;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/spotify")
    public ApiResponse<String> authWithSpotify() {
        String url = authService.authWithSpotify();
        return ApiResponse.success(url);
    }

    @GetMapping("/spotify/callback")
    public void callbackWithSpotify(@RequestParam String code,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        authService.handleSpotifyCallback(code, request);
        response.sendRedirect("http://127.0.0.1:5173");
    }

    @GetMapping("/youtube")
    public ApiResponse<String> authWithYoutube() {
        String url = authService.authWithYoutube();
        return ApiResponse.success(url);
    }

    @GetMapping("/youtube/callback")
    public void callbackWithYoutube(@RequestParam String code,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {
        authService.handleYoutubeCallback(code, request);
        response.sendRedirect("http://127.0.0.1:5173");
    }

    @GetMapping("/health")
    public ApiResponse<SessionStatus> hasSession(HttpServletRequest request) {
        SessionStatus status = authService.getSessionStatus(request);
        return ApiResponse.success(status);
    }

    @GetMapping("/session")
    public ApiResponse<SessionStatus> session(HttpServletRequest request) {
        SessionStatus status = authService.getSessionStatus(request);
        return ApiResponse.success(status);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponse.success(null);
    }
}
