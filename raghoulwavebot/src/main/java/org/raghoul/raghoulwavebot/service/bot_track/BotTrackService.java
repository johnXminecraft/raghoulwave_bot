package org.raghoul.raghoulwavebot.service.bot_track;

import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public interface BotTrackService {
    BotTrackDto getById(Integer id);
    List<BotTrackDto> getAll();
    BotTrackDto add(BotTrackDto botTrackDto);
    BotTrackDto update(BotTrackDto botTrackDto);
    void deleteById(Integer id);
    List<BotTrackDto> getByTitle(String title);
    List<BotTrackDto> getByArtist(String artist);
    List<BotTrackDto> getByAlbum(String album);
    BotTrackDto getBySpotifyId(String spotifyId);
    BotTrackDto getByYoutubeId(String youtubeId);
    List<BotTrackDto> getByUserIdAndState(Integer botUserId, String state);
    List<BotTrackDto> getByUserId(Integer botUserId);
    BotTrackDto spotifyTrackToBotTrackDto(BotUserDto botUserDto, Track track);
}
