package org.raghoul.raghoulwavebot.mapper.spotify_saved_tracks_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotify_saved_tracks_response.SpotifySavedTracksResponseDto;
import org.raghoul.raghoulwavebot.model.spotify_saved_tracks_response.SpotifySavedTracksResponse;
import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface SpotifySavedTracksResponseMapper {
    SpotifySavedTracksResponse dtoToEntity(SpotifySavedTracksResponseDto dto);
    SpotifySavedTracksResponseDto entityToDto(SpotifySavedTracksResponse entity);
    List<SpotifySavedTracksResponse> dtoListToEntityList(List<SpotifySavedTracksResponseDto> entities);
    List<SpotifySavedTracksResponseDto> entityListToDtoList(List<SpotifySavedTracksResponse> entities);
}
