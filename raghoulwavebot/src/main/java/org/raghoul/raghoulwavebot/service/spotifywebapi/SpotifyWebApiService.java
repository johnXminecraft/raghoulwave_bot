package org.raghoul.raghoulwavebot.service.spotifywebapi;

import org.raghoul.raghoulwavebot.dto.user.UserDto;

public interface SpotifyWebApiService {
    String getRecentlyPlayedTracks(UserDto user);
    String getSavedTracks(UserDto user);
}
