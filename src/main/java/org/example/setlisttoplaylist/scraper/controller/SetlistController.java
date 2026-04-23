package org.example.setlisttoplaylist.scraper.controller;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.example.setlisttoplaylist.scraper.dto.SetlistSearchResult;
import org.example.setlisttoplaylist.scraper.service.SetlistSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setlists")
public class SetlistController {

    private final SetlistSearchService searchService;

    @GetMapping("/search")
    public ApiResponse<SetlistSearchResult> searchSetlists(
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String venueName,
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        SetlistSearchResult result = searchService.search(artistName, cityName, venueName, year, page);
        return ApiResponse.success(result);
    }
}
