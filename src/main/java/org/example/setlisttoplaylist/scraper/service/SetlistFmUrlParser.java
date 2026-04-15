package org.example.setlisttoplaylist.scraper.service;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SetlistFmUrlParser {

    private static final Pattern SETLIST_ID_ONLY = Pattern.compile("^[0-9a-f]{8}$");

    private SetlistFmUrlParser() {
    }

    public static String extractSetlistId(String urlOrId) {
        if (urlOrId == null) {
            throw new IllegalArgumentException("setlist url is null");
        }

        String trimmed = urlOrId.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("setlist url is blank");
        }

        if (SETLIST_ID_ONLY.matcher(trimmed).matches()) {
            return trimmed;
        }

        URI uri = URI.create(trimmed);
        String path = uri.getPath();
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("missing URL path");
        }

        String[] segments = path.split("/");
        String lastSegment = segments[segments.length - 1];
        if (!lastSegment.endsWith(".html")) {
            throw new IllegalArgumentException("setlist URL does not end with .html");
        }

        String withoutHtml = lastSegment.substring(0, lastSegment.length() - ".html".length());
        int lastDash = withoutHtml.lastIndexOf('-');
        if (lastDash < 0) {
            throw new IllegalArgumentException("setlist URL does not contain an ID suffix");
        }

        String candidate = withoutHtml.substring(lastDash + 1);
        Matcher matcher = SETLIST_ID_ONLY.matcher(candidate);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid setlistId: " + candidate);
        }
        return candidate;
    }
}

