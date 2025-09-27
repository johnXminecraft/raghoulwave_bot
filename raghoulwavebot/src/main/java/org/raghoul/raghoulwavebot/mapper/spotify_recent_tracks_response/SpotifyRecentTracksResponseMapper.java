package org.raghoul.raghoulwavebot.mapper.spotify_recent_tracks_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotify_recent_tracks_response.SpotifyRecentTracksResponseDto;
import org.raghoul.raghoulwavebot.model.spotify_recent_tracks_response.SpotifyRecentTracksResponse;
import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface SpotifyRecentTracksResponseMapper {
    SpotifyRecentTracksResponse dtoToEntity(SpotifyRecentTracksResponseDto dto);
    SpotifyRecentTracksResponseDto entityToDto(SpotifyRecentTracksResponse entity);
    List<SpotifyRecentTracksResponse> dtoListToEntityList(List<SpotifyRecentTracksResponseDto> entities);
    List<SpotifyRecentTracksResponseDto> entityListToDtoList(List<SpotifyRecentTracksResponse> entities);
}
