package org.raghoul.raghoulwavebot.service.youtube_data_api;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeDataApiServiceImpl implements YoutubeDataApiService {

    private final YouTube youtube;

    @Override
    public String getYtMusicTrackId(String query) {
        try {
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(1L);
            SearchListResponse response = search.execute();
            List<SearchResult> results = response.getItems();
            if (results != null && !results.isEmpty()) {
                return results.getFirst().getId().getVideoId();
            } else {
                throw new IOException("Something went wrong... :(");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}
