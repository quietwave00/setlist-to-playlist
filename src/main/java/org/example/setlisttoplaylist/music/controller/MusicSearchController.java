package org.example.setlisttoplaylist.music.controller;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.example.setlisttoplaylist.auth.annotations.ProviderRole;
import org.example.setlisttoplaylist.music.service.PlaylistCreationService;
import org.example.setlisttoplaylist.music.service.dto.PlaylistCreationResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/music")
public class MusicSearchController {

    private final PlaylistCreationService service;

    @GetMapping
    public ApiResponse<PlaylistCreationResult> createPlaylist(
            @RequestParam String url,
            @ProviderRole String role
    ) {
        PlaylistCreationResult result = service.createPlaylistFromSetlist(url, role);
        return ApiResponse.success(result);
    }
}
