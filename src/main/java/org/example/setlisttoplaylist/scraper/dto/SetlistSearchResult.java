package org.example.setlisttoplaylist.scraper.dto;

import java.util.List;

public record SetlistSearchResult(
        List<SetlistSearchItem> setlists,
        int total,
        int page,
        int itemsPerPage
) {
}
