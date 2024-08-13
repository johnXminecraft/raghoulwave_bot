package org.raghoul.raghoulwavebot.mapper.spotifyresponse;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.model.spotifyresponse.SpotifyResponse;

@Mapper(
        componentModel = "spring"
)
public interface SpotifyResponseMapper {
    SpotifyResponseDTO spotifyResponseToSpotifyResponseDTO(SpotifyResponse entity);
    SpotifyResponse spotifyResponseDTOToSpotifyResponse(SpotifyResponseDTO dto);
}
