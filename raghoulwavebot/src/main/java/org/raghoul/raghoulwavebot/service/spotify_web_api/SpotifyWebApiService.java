package org.raghoul.raghoulwavebot.service.spotify_web_api;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_recent_tracks_response.SpotifyRecentTracksResponseDto;
import org.raghoul.raghoulwavebot.dto.spotify_saved_tracks_response.SpotifySavedTracksResponseDto;

public interface SpotifyWebApiService {
    SpotifyRecentTracksResponseDto getRecentlyPlayedTracks(BotUserDto botUserDto);
    SpotifySavedTracksResponseDto getSavedTracks(BotUserDto botUserDto);
    SpotifyCurrentTrackResponseDto getCurrentTrack(BotUserDto botUserDto);
    String extractSpotifyTrackId(String url);
}
