package org.raghoul.raghoulwavebot.mapper.spotify_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotify_response.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.model.spotify_response.SpotifyResponse;

@Mapper(
        componentModel = "spring"
)
public interface SpotifyResponseMapper {
    SpotifyResponseDTO spotifyResponseToSpotifyResponseDTO(SpotifyResponse entity);
    SpotifyResponse spotifyResponseDTOToSpotifyResponse(SpotifyResponseDTO dto);
}
