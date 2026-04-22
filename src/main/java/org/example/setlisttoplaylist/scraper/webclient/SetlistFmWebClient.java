package org.example.setlisttoplaylist.scraper.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.global.exception.ApiException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm.SetlistFmSetlistResponse;
import org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm.SetlistFmSetlistsResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetlistFmWebClient {

    private final WebClient setlistFmClient;

    public SetlistFmSetlistResponse getSetlist(String setlistId) {
        return setlistFmClient.get()
                .uri("/setlist/{setlistId}", setlistId)
                .retrieve()
                .onStatus(status -> status.value() == 403, this::forbidden)
                .onStatus(status -> status.value() == 404, this::notFound)
                .onStatus(HttpStatusCode::isError, this::upstreamError)
                .bodyToMono(SetlistFmSetlistResponse.class)
                .block(Duration.ofSeconds(10));
    }

    public SetlistFmSetlistsResponse searchSetlists(
            String artistName,
            String cityName,
            String venueName,
            String year,
            int page
    ) {
        return setlistFmClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/search/setlists")
                            .queryParam("p", page);

                    if (artistName != null) {
                        builder.queryParam("artistName", artistName);
                    }
                    if (cityName != null) {
                        builder.queryParam("cityName", cityName);
                    }
                    if (venueName != null) {
                        builder.queryParam("venueName", venueName);
                    }
                    if (year != null) {
                        builder.queryParam("year", year);
                    }
                    return builder.build();
                })
                .exchangeToMono(response -> handleSearchResponse(response, page))
                .block(Duration.ofSeconds(10));
    }

    private Mono<SetlistFmSetlistsResponse> handleSearchResponse(ClientResponse response, int page) {
        if (response.statusCode().value() == 404) {
            return Mono.just(new SetlistFmSetlistsResponse(List.of(), 0, page, 20));
        }
        if (response.statusCode().value() == 403) {
            return Mono.error(new ApiException(ErrorCode.SETLIST_FM_FORBIDDEN));
        }
        if (response.statusCode().isError()) {
            return Mono.error(new ApiException(ErrorCode.SETLIST_FM_UPSTREAM_ERROR));
        }
        return response.bodyToMono(SetlistFmSetlistsResponse.class);
    }

    private Mono<Throwable> forbidden(ClientResponse response) {
        return Mono.error(new ApiException(ErrorCode.SETLIST_FM_FORBIDDEN));
    }

    private Mono<Throwable> notFound(ClientResponse response) {
        return Mono.error(new ApiException(ErrorCode.SETLIST_FM_NOT_FOUND));
    }

    private Mono<Throwable> upstreamError(ClientResponse response) {
        return Mono.error(new ApiException(ErrorCode.SETLIST_FM_UPSTREAM_ERROR));
    }
}
