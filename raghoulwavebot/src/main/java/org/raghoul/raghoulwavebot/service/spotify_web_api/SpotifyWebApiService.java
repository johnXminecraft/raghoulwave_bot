package org.raghoul.raghoulwavebot.service.spotify_web_api;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Track;

public interface SpotifyWebApiService {
    String getRecentlyPlayedTracks(BotUserDto botUserDto);
    String getSavedTracks(BotUserDto botUserDto);
    SpotifyCurrentTrackResponseDto getCurrentTrack(BotUserDto botUserDto);
}
