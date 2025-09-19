package org.raghoul.raghoulwavebot.mapper.spotify_current_track_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotify_current_track_response.SpotifyCurrentTrackResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.model.spotify_current_track_response.SpotifyCurrentTrackResponse;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {BotTrackMapper.class}
)
public interface SpotifyCurrentTrackResponseMapper {
    SpotifyCurrentTrackResponse dtoToEntity(SpotifyCurrentTrackResponseDto dto);
    SpotifyCurrentTrackResponseDto entityToDto(SpotifyCurrentTrackResponse entity);
    List<SpotifyCurrentTrackResponse> dtoListToEntityList(List<SpotifyCurrentTrackResponse> entities);
    List<SpotifyCurrentTrackResponseDto> entityListToDtoList(List<SpotifyCurrentTrackResponse> entities);
}
