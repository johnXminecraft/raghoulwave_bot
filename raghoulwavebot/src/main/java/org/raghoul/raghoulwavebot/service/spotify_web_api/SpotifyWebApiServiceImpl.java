package org.raghoul.raghoulwavebot.service.spotify_web_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_recent_tracks_response.SpotifyRecentTracksResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_saved_tracks_response.SpotifySavedTracksResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_current_track_response.SpotifyCurrentTrackResponseMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_recent_tracks_response.SpotifyRecentTracksResponseMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_saved_tracks_response.SpotifySavedTracksResponseMapper;
import org.raghoul.raghoulwavebot.model.spotify_current_track_response.SpotifyCurrentTrackResponse;
import org.raghoul.raghoulwavebot.model.spotify_recent_tracks_response.SpotifyRecentTracksResponse;
import org.raghoul.raghoulwavebot.model.spotify_saved_tracks_response.SpotifySavedTracksResponse;
import org.raghoul.raghoulwavebot.service.bot_track.BotTrackService;
import org.raghoul.raghoulwavebot.service.spotify_web_api_authorization.SpotifyWebApiAuthorizationService;
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
import java.util.*;

@SuppressWarnings("StringBufferReplaceableByString")
@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyWebApiServiceImpl implements SpotifyWebApiService {

    private final SpotifyWebApiAuthorizationService spotifyWebApiAuthorizationService;
    private final SpotifyCurrentTrackResponseMapper spotifyCurrentTrackResponseMapper;
    private final BotTrackMapper botTrackMapper;
    private final BotTrackService botTrackService;
    private final SpotifySavedTracksResponseMapper spotifySavedTracksResponseMapper;
    private final SpotifyRecentTracksResponseMapper spotifyRecentTracksResponseMapper;

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
                BotTrackDto botTrackDto = botTrackService.spotifyTrackToBotTrackDto(botUserDto, track, "current");

                return spotifyCurrentTrackResponseMapper.entityToDto(
                        SpotifyCurrentTrackResponse.builder()
                                .responseCode(200)
                                .botTrack(botTrackMapper.dtoToEntity(botTrackDto))
                                .output(
                                        "Here is the track you are currently listening to :)\n\n" +
                                        "<a href='" +
                                        "https://open.spotify.com/track/" + botTrackDto.getSpotifyId() +
                                        "'>" +
                                        botTrackDto.getArtist() + " - " + botTrackDto.getTitle() + "</a>\n"
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
    public SpotifySavedTracksResponseDto getSavedTracks(BotUserDto botUserDto) {
        // getting spotify access token and spotify api object
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        // creating request for user's saved tracks
        GetUsersSavedTracksRequest request = spotifyApi.getUsersSavedTracks()
                .offset(0)
                .limit(50)
                .build();

        try {
            // executing request and getting user's saved tracks
            Paging<SavedTrack> savedTrackPaging = request.execute();
            SavedTrack[] savedTracks = savedTrackPaging.getItems();

            // getting tracks, adding them to response object
            for(SavedTrack savedTrackItem : savedTracks) {
                botTrackService.spotifyTrackToBotTrackDto(botUserDto, savedTrackItem.getTrack(), "saved");
            }

            return spotifySavedTracksResponseMapper.entityToDto(
                    SpotifySavedTracksResponse.builder()
                            .responseCode(200)
                            .output("Here are your saved tracks :)\n\n")
                            .build()
            );
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            /* TODO
            *   make custom exceptions man */
            System.out.println(e.getMessage());

            return spotifySavedTracksResponseMapper.entityToDto(
                    SpotifySavedTracksResponse.builder()
                            .responseCode(500)
                            .output(e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public SpotifyRecentTracksResponseDto getRecentlyPlayedTracks(BotUserDto botUserDto) {
        // getting spotify access token and spotify api object
        String accessToken = spotifyWebApiAuthorizationService.authorizationCodeRefresh_Sync(botUserDto);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        // creating request for user's recent tracks
        long currentDate = new Date().getTime();
        GetCurrentUsersRecentlyPlayedTracksRequest request = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                .limit(50)
                .before(new Date(currentDate))
                .build();

        try {
            // executing request and getting user's saved tracks
            PagingCursorbased<PlayHistory> playHistoryPagingCursorbased = request.execute();
            PlayHistory[] playHistory = playHistoryPagingCursorbased.getItems();

            // getting tracks, adding them to response object
            for(PlayHistory playHistoryItem : playHistory) {
                botTrackService.spotifyTrackToBotTrackDto(botUserDto, playHistoryItem.getTrack(),"recent");
            }

            return spotifyRecentTracksResponseMapper.entityToDto(
                    SpotifyRecentTracksResponse.builder()
                            .responseCode(200)
                            .output("Here are your recent tracks :)\n\n")
                            .build()
            );
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            System.out.println(e.getMessage());

            return spotifyRecentTracksResponseMapper.entityToDto(
                    SpotifyRecentTracksResponse.builder()
                            .responseCode(500)
                            .output(e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public String extractSpotifyTrackId(String url) {
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
}
