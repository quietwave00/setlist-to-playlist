package org.example.setlisttoplaylist.scraper.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.global.exception.ApiException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm.SetlistFmSetlistResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetlistFmWebClient {

    private final WebClient setlistFmClient;

    public SetlistFmSetlistResponse getSetlist(String setlistId) {
        return setlistFmClient.get()
                .uri("/setlist/{setlistId}", setlistId)
                .retrieve()
                .onStatus(status -> status.value() == 403,
                        resp -> Mono.error(new ApiException(ErrorCode.SETLIST_FM_FORBIDDEN)))
                .onStatus(status -> status.value() == 404,
                        resp -> Mono.error(new ApiException(ErrorCode.SETLIST_FM_NOT_FOUND)))
                .onStatus(HttpStatusCode::isError,
                        resp -> Mono.error(new ApiException(ErrorCode.SETLIST_FM_UPSTREAM_ERROR)))
                .bodyToMono(SetlistFmSetlistResponse.class)
                .block(Duration.ofSeconds(10));
    }
}
