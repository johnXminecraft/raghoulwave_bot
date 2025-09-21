package org.raghoul.raghoulwavebot.service.audiotag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioTagServiceImpl implements AudioTagService {

    @Override
    public void setTrackTags(BotTrackDto botTrackDto, File file) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTagOrCreateAndSetDefault();
            tag.setField(FieldKey.ARTIST, botTrackDto.getArtist());
            tag.setField(FieldKey.ALBUM, botTrackDto.getAlbum());
            tag.setField(FieldKey.TITLE, botTrackDto.getTitle());
            audioFile.commit();
        } catch (Exception e) {
            System.out.println("[jaudiotagger] " + e.getMessage());
        }
    }
}
