package org.example.setlisttoplaylist.scraper.service;

import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.scraper.dto.SetlistScrapResult;
import org.example.setlisttoplaylist.scraper.dto.SetlistTrack;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsoupSetlistScraperService implements SetlistScraperService {

    @Override
    public SetlistScrapResult scrapeTracks(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            String artist = extractArtistFromUrl(url);
            Elements rows = doc.select(".songLabel");
            List<SetlistTrack> tracks = rows.stream()
                    .map(e -> new SetlistTrack(e.text()))
                    .toList();
            int total = tracks.size();
            String setlistTitle = extractSetlistTitleFromUrl(url);

            return new SetlistScrapResult(artist, tracks, total, setlistTitle);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.SCRAPING_FAILED);
        }
    }

    private String extractArtistFromUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        String[] segments = path.split("/");

        if (segments.length > 2) {
            String raw = segments[2];
            String nameWithSpace = raw.replace("-", " ");
            return toTitleCase(nameWithSpace);
        }
        return "UnknownArtist";
    }


    private String extractSetlistTitleFromUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        String[] segments = path.split("/");

        if (segments.length > 4) {
            String raw = segments[4];
            String titleWithSpace = raw.replace("-", " ");
            return toTitleCase(titleWithSpace);
        }
        return "UnknownTitle";
    }

    private String toTitleCase(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        return Arrays.stream(input.split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() +
                        word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
