package org.raghoul.raghoulwavebot.mapper.spotify_authorization_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotify_authorization_response.SpotifyAuthorizationResponseDto;
import org.raghoul.raghoulwavebot.model.spotify_authorization_response.SpotifyAuthorizationResponse;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface SpotifyAuthorizationResponseMapper {
    SpotifyAuthorizationResponseDto entityToDto(SpotifyAuthorizationResponse entity);
    SpotifyAuthorizationResponse dtoToEntity(SpotifyAuthorizationResponseDto dto);
    List<SpotifyAuthorizationResponseDto> entityListToDtoList(List<SpotifyAuthorizationResponse> entities);
    List<SpotifyAuthorizationResponse> dtoListToEntityList(List<SpotifyAuthorizationResponseDto> dtos);
}
