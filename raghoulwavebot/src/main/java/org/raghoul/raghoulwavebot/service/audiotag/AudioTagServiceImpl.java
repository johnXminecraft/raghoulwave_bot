package org.raghoul.raghoulwavebot.service.audiotag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.service.spotify_web_api.SpotifyWebApiService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioTagServiceImpl implements AudioTagService {

    private final SpotifyWebApiService spotifyWebApiService;

    @Override
    public void setTrackTags(BotUserDto botUser, IPlaylistItem item, File file) {
        Track track = spotifyWebApiService.getTrackMetadata(botUser, item);
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTagOrCreateAndSetDefault();
            tag.setField(FieldKey.ARTIST, track.getArtists()[0].getName());
            tag.setField(FieldKey.ALBUM, track.getAlbum().getName());
            tag.setField(FieldKey.TITLE, track.getName());
            audioFile.commit();
        } catch (Exception e) {
            System.out.println("[jaudiotagger] " + e.getMessage());
        }
    }
}
