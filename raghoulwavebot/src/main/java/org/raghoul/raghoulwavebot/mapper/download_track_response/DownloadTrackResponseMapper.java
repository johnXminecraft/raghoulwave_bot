package org.raghoul.raghoulwavebot.mapper.download_track_response;

import org.mapstruct.Mapper;
import org.raghoul.raghoulwavebot.dto.download_track_response.DownloadTrackResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.model.download_track_response.DownloadTrackResponse;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {BotTrackMapper.class}
)
public interface DownloadTrackResponseMapper {
    DownloadTrackResponse dtoToEntity(DownloadTrackResponseDto dto);
    DownloadTrackResponseDto entityToDto(DownloadTrackResponse entity);
    List<DownloadTrackResponse> dtoListToEntityList(List<DownloadTrackResponseDto> entities);
    List<DownloadTrackResponseDto> entityListToDtoList(List<DownloadTrackResponse> entities);
}
