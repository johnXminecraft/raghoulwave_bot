package org.raghoul.raghoulwavebot.service.spotify_web_api;

import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Track;

public interface SpotifyWebApiService {
    Track getTrackMetadata(BotUserDto botUserDto, IPlaylistItem item);
    boolean doesTrackExist(BotUserDto botUserDto, IPlaylistItem item);
    String getRecentlyPlayedTracks(BotUserDto botUserDto);
    String getSavedTracks(BotUserDto botUserDto);
    String getCurrentTrack(BotUserDto botUserDto);
}
