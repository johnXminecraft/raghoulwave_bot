package org.raghoul.raghoulwavebot.service.download;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.bot_track.BotTrackDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.dto.download_track_response.DownloadTrackResponseDto;
import org.raghoul.raghoulwavebot.mapper.bot_track.BotTrackMapper;
import org.raghoul.raghoulwavebot.mapper.download_track_response.DownloadTrackResponseMapper;
import org.raghoul.raghoulwavebot.model.download_track_response.DownloadTrackResponse;
import org.raghoul.raghoulwavebot.service.audiotag.AudioTagService;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {
    private final String downloadPath;
    private final String botToken;
    private final AudioTagService audioTagService;
    private final BotTrackMapper botTrackMapper;
    private final DownloadTrackResponseMapper downloadTrackResponseMapper;

    @Override
    public DownloadTrackResponseDto sendTrack(BotUserDto botUserDto, BotTrackDto botTrackDto) {
        // getting track's path after downloading it
        String trackPath = downloadTrack(botTrackDto);
        // setting correct mp3 tags
        audioTagService.setTrackTags(botTrackDto, new File(trackPath));
        try {
            // checking if path is correct
            if(trackPath.startsWith(downloadPath)) {
                // preparing command to send an mp3 file
                String command = "curl";
                String param = "-F";
                String param1key1 = "chat_id=" + botUserDto.getTelegramId();
                String param1key2 = "audio=@" + trackPath;
                String request = "https://api.telegram.org/bot" + botToken + "/sendAudio";
                try {
                    // executing the command
                    ProcessBuilder processBuilder = new ProcessBuilder(
                            command,
                            param, param1key1,
                            param, param1key2,
                            request
                    );
                    /* TODO
                    *   logging */
                    // logging
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("[curl] " + line);
                        }
                    }
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        System.out.println(processBuilder.command().toString());
                        /* TODO
                         *   make custom exceptions */
                        // CUrlException
                        throw new RuntimeException("Request failed with exit code " + exitCode);
                    }
                    if(!clearDirectory()) {
                        /* TODO
                         *   make custom exceptions */
                        // ClearDirectoryException
                        throw new RuntimeException("Failed to clear directory");
                    }
                    DownloadTrackResponse downloadTrackResponse = DownloadTrackResponse.builder()
                            .responseCode(200)
                            .botTrack(botTrackMapper.dtoToEntity(botTrackDto))
                            .output(
                                    "<a href='https://open.spotify.com/track/" + botTrackDto.getSpotifyId() + "'>" +
                                    botTrackDto.getArtist() + " - " + botTrackDto.getTitle() + " (" +
                                    botTrackDto.getAlbum() + ")" + "</a> downloaded successfully!/n"
                            )
                            .build();
                    return downloadTrackResponseMapper.entityToDto(downloadTrackResponse);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    DownloadTrackResponse downloadTrackResponse = DownloadTrackResponse.builder()
                            .responseCode(404)
                            .botTrack(botTrackMapper.dtoToEntity(botTrackDto))
                            .output(
                                    "<a href='https://open.spotify.com/track/" + botTrackDto.getSpotifyId() + "'>" +
                                    botTrackDto.getArtist() + " - " + botTrackDto.getTitle() + " (" +
                                    botTrackDto.getAlbum() + ")" + "</a> download has failed./n"
                            )
                            .build();
                    return downloadTrackResponseMapper.entityToDto(downloadTrackResponse);
                }
            } else {
                throw new RuntimeException("Invalid track path");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            DownloadTrackResponse downloadTrackResponse = DownloadTrackResponse.builder()
                    .responseCode(404)
                    .botTrack(botTrackMapper.dtoToEntity(botTrackDto))
                    .output(
                            "<a href='https://open.spotify.com/track/" + botTrackDto.getSpotifyId() + "'>" +
                            botTrackDto.getArtist() + " - " + botTrackDto.getTitle() + " (" +
                            botTrackDto.getAlbum() + ")" + "</a> download has failed./n"
                    )
                    .build();
            return downloadTrackResponseMapper.entityToDto(downloadTrackResponse);
        }
    }

    private String downloadTrack(BotTrackDto botTrackDto) {
        String command = "yt-dlp";
        String type = "-t";
        String typeName = "mp3";
        String format = "-f";
        String formatType = "bestaudio";
        String pathArg = "-o";
        String path = downloadPath + "/track";
        String link = "https://music.youtube.com/watch?v=" + botTrackDto.getYoutubeId();
        Path downloadDir = Paths.get(downloadPath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    command,
                    type, typeName,
                    format, formatType,
                    pathArg, path,
                    link
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[yt-dlp] " + line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println(processBuilder.command().toString());
                /* TODO
                 *   make custom exceptions */
                // YtDlpException
                throw new RuntimeException("yt-dlp failed with exit code " + exitCode);
            }
            File[] files = downloadDir.toFile().listFiles((_, name) -> name.endsWith(".mp3"));
            if (files == null || files.length == 0) {
                /* TODO
                 *   make custom exceptions */
                // Mp3CreationException
                throw new RuntimeException("No mp3 file was created.");
            }
            File latestFile = files[0];
            for (File f : files) {
                if (f.lastModified() > latestFile.lastModified()) {
                    latestFile = f;
                }
            }
            if (!latestFile.exists()) {
                /* TODO
                 *   make custom exceptions */
                // FileNotFoundException
                throw new RuntimeException("File not found after download.");
            }
            return latestFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    private boolean clearDirectory() {
        File dir = new File(downloadPath);
        if (dir.isDirectory()) {
            Objects.requireNonNull(dir.listFiles());
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isFile()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        return false;
                    }
                    System.out.println("Deleted: " + file.getName() + " -> " + deleted);
                }
            }
        }
        return true;
    }
}
