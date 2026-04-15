package org.example.setlisttoplaylist.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.AuthService;
import org.example.setlisttoplaylist.auth.service.dto.SessionStatus;
import org.example.setlisttoplaylist.config.AppProperties;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AppProperties appProperties;
    private final AuthService authService;

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
        response.sendRedirect(appProperties.getDomain() + "/?authProvider=youtube");
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
