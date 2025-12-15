package org.example.setlisttoplaylist.scraper.service;

import org.example.setlisttoplaylist.scraper.dto.SetlistScrapResult;

public interface SetlistScraperService {

    SetlistScrapResult scrapeTracks(String setlistUrl);

}
