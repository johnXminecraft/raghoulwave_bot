package org.raghoul.raghoulwavebot.service.spotify_web_api;

import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.bot_user_track.BotUserTrackDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_current_track_response.SpotifyCurrentTrackResponseMapper;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.model.composite_key.bot_user_track.BotUserTrackId;
import org.raghoul.raghoulwavebot.model.spotify_current_track_response.SpotifyCurrentTrackResponse;
import org.raghoul.raghoulwavebot.service.bot_track.BotTrackService;
import org.raghoul.raghoulwavebot.service.bot_user_track.BotUserTrackService;
import org.raghoul.raghoulwavebot.service.download.DownloadService;
import org.raghoul.raghoulwavebot.service.spotify_web_api_authorization.SpotifyWebApiAuthorizationService;
import org.raghoul.raghoulwavebot.service.youtube_data_api.YoutubeDataApiService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("StringBufferReplaceableByString")
@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyWebApiServiceImpl implements SpotifyWebApiService {

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyCurrentTrackResponseMapper spotifyCurrentTrackResponseMapper;
    private final YoutubeDataApiService youtubeDataApiService;
    private final BotTrackMapper botTrackMapper;
    private final BotTrackService botTrackService;
    private final BotUserTrackService botUserTrackService;

    @Override
    public SpotifyCurrentTrackResponseDto getCurrentTrack(BotUserDto botUserDto) {
        // getting spotify access token and spotify api object
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        // creating request for user's currently playing track
        GetUsersCurrentlyPlayingTrackRequest request = spotifyApi.getUsersCurrentlyPlayingTrack()
                .additionalTypes("track")
                .build();

        try {
            CurrentlyPlaying currentlyPlaying = request.execute();
            // checking if request is successful
            if(Objects.isNull(currentlyPlaying)) {
                throw new SpotifyWebApiException("Nothing is playing :(");
            } else {
                // getting track info and saving it to db
                Track track = getTrackMetadata(botUserDto, currentlyPlaying.getItem());

                String title = Objects.requireNonNull(track).getName();
                String artist = Objects.requireNonNull(Arrays.stream(
                        track.getArtists()).findFirst().orElse(null)).getName();
                String album = track.getAlbum().getName();
                String spotifyId = extractSpotifyTrackId(currentlyPlaying.getItem().getExternalUrls().get("spotify"));
                String youtubeId = youtubeDataApiService.getYtMusicTrackId(title + " " + artist);

                BotTrack botTrack = BotTrack.builder()
                                            .title(title)
                                            .artist(artist)
                                            .album(album)
                                            .spotifyId(spotifyId)
                                            .youtubeId(youtubeId)
                                            .build();

                BotTrackDto botTrackDto = botTrackMapper.entityToDto(botTrack);
                botTrackDto = botTrackService.add(botTrackDto);

                // creating relation between track and user
                BotUserTrackId id = new BotUserTrackId(botUserDto.getId(), botTrackDto.getId(), "current");
                BotUserTrackDto botUserTrackDto = new BotUserTrackDto();
                botUserTrackDto.setId(id);
                botUserTrackDto.setBotUser(botUserDto);
                botUserTrackDto.setBotTrack(botTrackDto);
                botUserTrackService.add(botUserTrackDto);

                return spotifyCurrentTrackResponseMapper.entityToDto(
                        SpotifyCurrentTrackResponse.builder()
                                .responseCode(200)
                                .botTrack(botTrack)
                                .output(
                                        "Current track:\n\n" +
                                        "<a href='" +
                                        "https://open.spotify.com/track/" + spotifyId +
                                        "'>" +
                                        artist + " " + title + " (" + album + ")" +
                                        "</a>\n"
                                )
                                .build()
                );
            }
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            // returning error-response
            return spotifyCurrentTrackResponseMapper.entityToDto(
                    SpotifyCurrentTrackResponse.builder()
                            .responseCode(404)
                            .botTrack(null)
                            .output(e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public String getSavedTracks(BotUserDto botUserDto) {
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        GetUsersSavedTracksRequest request = spotifyApi.getUsersSavedTracks().
                limit(10)
                .offset(0)
                .market(CountryCode.UA)
                .build();
        try {
            Paging<SavedTrack> savedTrackPaging = request.execute();
            SavedTrack[] savedTrack = savedTrackPaging.getItems();
            StringBuilder outputBuilder = new StringBuilder();

            for(SavedTrack savedTrackItem : savedTrack) {
                outputBuilder
                        .append("<a href='")
                        .append(savedTrackItem.getTrack().getExternalUrls().get("spotify"))
                        .append("'>")
                        .append(savedTrackItem.getTrack().getName())
                        .append("</a>\n");
            }

            String output = outputBuilder.toString();

            return "Saved tracks:\n\n" + output;
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }

    @Override
    public String getRecentlyPlayedTracks(BotUserDto botUserDto) {

        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        long currentDate = new Date().getTime();

        GetCurrentUsersRecentlyPlayedTracksRequest request = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                        .before(new Date(currentDate))
                        .limit(10)
                        .build();

        try {
            PagingCursorbased<PlayHistory> playHistoryPagingCursorbased = request.execute();

            PlayHistory[] playHistory = playHistoryPagingCursorbased.getItems();

            StringBuilder outputBuilder = new StringBuilder();

            for(PlayHistory playHistoryItem : playHistory) {
                outputBuilder
                        .append("<a href='")
                        .append(playHistoryItem.getTrack().getExternalUrls().get("spotify"))
                        .append("'>")
                        .append(playHistoryItem.getTrack().getName())
                        .append("</a>\n");
            }

            String output = outputBuilder.toString();

            return "Recent tracks:\n\n" + output;
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return "Error";
        }
    }

    private Track getTrackMetadata(BotUserDto botUserDto, IPlaylistItem item) {
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        String spotifyUrl = item.getExternalUrls().get("spotify");
        String trackId = extractSpotifyTrackId(spotifyUrl);
        GetTrackRequest request = spotifyApi.getTrack(trackId).build();
        try {
            Track track = request.execute();
            if(Objects.isNull(track)) {
                throw new SpotifyWebApiException("No such track found :(");
            } else {
                return track;
            }
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String extractSpotifyTrackId(String url) {
        try {
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("Invalid track URL :(");
            }
            int lastSlash = url.lastIndexOf("/");
            if (lastSlash == -1 || lastSlash == url.length() - 1) {
                throw new IllegalArgumentException("Invalid track URL :(");
            }
            String idPart = url.substring(lastSlash + 1);
            String[] parts = idPart.split("\\?");
            if (parts.length == 0 || parts[0].isBlank()) {
                throw new IllegalArgumentException("Invalid track URL :(");
            }
            return parts[0];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    private boolean doesTrackExist(BotUserDto botUserDto, IPlaylistItem item) {
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        String spotifyUrl = item.getExternalUrls().get("spotify");
        String trackId = extractSpotifyTrackId(spotifyUrl);
        GetTrackRequest request = spotifyApi.getTrack(trackId).build();
        try {
            Track track = request.execute();
            if(Objects.isNull(track)) {
                throw new SpotifyWebApiException("No such track found :(");
            } else {
                return true;
            }
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
