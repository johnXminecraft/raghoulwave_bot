package org.raghoul.raghoulwavebot.service.spotify_web_api;

import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_saved_tracks_response.SpotifySavedTracksResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_current_track_response.SpotifyCurrentTrackResponseMapper;
import org.raghoul.raghoulwavebot.mapper.spotify_saved_tracks_response.SpotifySavedTracksResponseMapper;
import org.raghoul.raghoulwavebot.model.bot_track.BotTrack;
import org.raghoul.raghoulwavebot.model.spotify_current_track_response.SpotifyCurrentTrackResponse;
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

                BotTrackDto botTrackDto = botTrackService.spotifyTrackToBotTrackDto(botUserDto, track);

                return spotifyCurrentTrackResponseMapper.entityToDto(
                        SpotifyCurrentTrackResponse.builder()
                                .responseCode(200)
                                .botTrack(botTrackMapper.dtoToEntity(botTrackDto))
                                .output(
                                        "Current track:\n\n" +
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
        GetUsersSavedTracksRequest request = spotifyApi.getUsersSavedTracks().
                limit(10)
                .offset(0)
                /* TODO
                *   make different markets for different users */
                .market(CountryCode.UA)
                .build();

        try {
            // executing request and getting user's saved tracks
            Paging<SavedTrack> savedTrackPaging = request.execute();
            SavedTrack[] savedTrack = savedTrackPaging.getItems();

            // creating list for all saved tracks
            List<BotTrack> botTracks = new ArrayList<>();

            String output = "Here are your saved tracks :)/n/n";
            StringBuilder outputBuilder = new StringBuilder();

            // getting tracks, adding them to response object, creating output
            for(SavedTrack savedTrackItem : savedTrack) {
                Track track = savedTrackItem.getTrack();
                BotTrackDto botTrackDto = botTrackService.spotifyTrackToBotTrackDto(botUserDto, track);
                BotTrack botTrack = botTrackMapper.dtoToEntity(botTrackDto);
                botTracks.add(botTrack);
                outputBuilder.append("<a href='https://open.spotify.com/track/")
                        .append(botTrackDto.getSpotifyId())
                        .append("'>")
                        .append(botTrackDto.getArtist())
                        .append(" ")
                        .append(botTrackDto.getTitle())
                        .append("</a>\n");
                output = outputBuilder.toString();
            }

            return spotifySavedTracksResponseMapper.entityToDto(
                    SpotifySavedTracksResponse.builder()
                            .responseCode(200)
                            .botTracks(botTracks)
                            .output(output)
                            .build()
            );
        } catch (SpotifyWebApiException | IOException | ParseException e) {
            /* TODO
            *   make custom exceptions man */
            System.out.println(e.getMessage());

            return spotifySavedTracksResponseMapper.entityToDto(
                    SpotifySavedTracksResponse.builder()
                            .responseCode(200)
                            .botTracks(null)
                            .output(e.getMessage())
                            .build()
            );
        }
    }

    /* TODO
    *   finish this one */
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
