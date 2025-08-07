package org.raghoul.raghoulwavebot.service.download;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.spotifywebapi.SpotifyWebApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    private final SpotifyWebApiService spotifyWebApiService;
    @Value("${raghoulwavebot.config.google.youtube_data_api_v3.api_key}")
    private String ytDataApiV3Key;

    @Override
    public String getYtMusicTrackLink(UserDto user, IPlaylistItem item) {
        if(!spotifyWebApiService.doesTrackExist(user, item)) {
            return "No such track found :(";
        }
        Track track = spotifyWebApiService.getTrackMetadata(user, item);
        String query = track.getName() + " " + track.getArtists()[0].getName();
        String ytMusicTrackId = getYtMusicTrackId(query);
        if(!Objects.equals(ytMusicTrackId, "Something went wrong... :(")) {
            return ytMusicTrackId;
        }
        return "https://music.youtube.com/watch?v=" + ytMusicTrackId;
    }

    private String getYtMusicTrackId(String query) {
        try {
            YouTube youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), // something is wrong here
                    null
            ).setApplicationName("link-receiver").build();
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(ytDataApiV3Key);
            search.setQ(query + " site:music.youtube.com");
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
