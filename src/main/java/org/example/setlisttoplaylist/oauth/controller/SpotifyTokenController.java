package org.example.setlisttoplaylist.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.example.setlisttoplaylist.oauth.SpotifyStaticAccessTokenManager;
import org.example.setlisttoplaylist.oauth.dto.SpotifyAccessTokenInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spotify/token")
public class SpotifyTokenController {

    private final SpotifyStaticAccessTokenManager accessTokenManager;

    @PostMapping("/refresh")
    public ApiResponse<SpotifyAccessTokenInfo> refreshAccessToken() {
        SpotifyAccessTokenInfo tokenInfo = accessTokenManager.getAccessTokenInfo();
        return ApiResponse.success(tokenInfo);
    }
}
