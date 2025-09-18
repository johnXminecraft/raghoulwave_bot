package org.raghoul.raghoulwavebot.service.spotify_web_api;

import org.raghoul.raghoulwavebot.dto.user.UserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.Track;

public interface SpotifyWebApiService {
    Track getTrackMetadata(UserDto user, IPlaylistItem item);
    boolean doesTrackExist(UserDto user, IPlaylistItem item);
    String getRecentlyPlayedTracks(UserDto user);
    String getSavedTracks(UserDto user);
    CurrentlyPlaying getCurrentTrack(UserDto user);
    boolean isSomethingPlayingCurrently(UserDto user);
}
