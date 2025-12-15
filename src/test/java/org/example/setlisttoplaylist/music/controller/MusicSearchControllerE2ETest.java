package org.example.setlisttoplaylist.music.controller;

import org.example.setlisttoplaylist.auth.annotations.ProviderRoleArgumentResolver;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.service.PlaylistCreationService;
import org.example.setlisttoplaylist.music.service.dto.PlaylistCreationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MusicSearchControllerE2ETest {

    @Mock
    private PlaylistCreationService playlistCreationService;

    @InjectMocks
    private MusicSearchController controller;

    private MockMvc mockMvcWithSecurity() {
        // Set a mock authenticated user with ROLE_SPOTIFY so the argument resolver can pick it up.
        var auth = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_SPOTIFY"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new ProviderRoleArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("GET /api/music/create returns playlist creation result when authenticated with provider role")
    void createPlaylist_returnsPlaylistResult() throws Exception {
        MockMvc mockMvc = mockMvcWithSecurity();

        // given
        String setlistUrl = "https://www.setlist.fm/setlist/machine-girl/2025/shibuya-club-quattro-tokyo-japan-134fb985.html";
        var track = new Track("Song A", "Artist X", "track123", "https://provider/track123");
        var result = new PlaylistCreationResult(
                "Artist X",
                1,
                "playlist-abc",
                "https://open.spotify.com/playlist/playlist-abc",
                "spotify",
                List.of(track)
        );
        given(playlistCreationService.createPlaylistFromSetlist(eq(setlistUrl), anyString()))
                .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/music/create")
                        .param("url", setlistUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.data.artist").value("Artist X"))
                .andExpect(jsonPath("$.data.totalTracks").value(1))
                .andExpect(jsonPath("$.data.playlistId").value("playlist-abc"))
                .andExpect(jsonPath("$.data.playlistUrl").value("https://open.spotify.com/playlist/playlist-abc"))
                .andExpect(jsonPath("$.data.provider").value("spotify"))
                .andExpect(jsonPath("$.data.tracks[0].title").value("Song A"))
                .andExpect(jsonPath("$.data.tracks[0].artist").value("Artist X"))
                .andExpect(jsonPath("$.data.tracks[0].providerTrackId").value("track123"))
                .andExpect(jsonPath("$.data.tracks[0].providerLink").value("https://provider/track123"));
    }
}
