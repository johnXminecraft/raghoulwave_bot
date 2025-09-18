package org.raghoul.raghoulwavebot.service.download;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.audiotag.AudioTagService;
import org.raghoul.raghoulwavebot.service.spotify_web_api.SpotifyWebApiService;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Track;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    private final YouTube youtube;
    private final String downloadPath;
    private final String botToken;
    private final SpotifyWebApiService spotifyWebApiService;
    private final AudioTagService audioTagService;

    @Override
    public String sendTrack(UserDto user, IPlaylistItem item) {
        String trackPath = downloadTrack(user, item);
        audioTagService.setTrackTags(user, item, new File(trackPath));
        try {
            if(trackPath.startsWith(downloadPath)) {
                String command = "curl";
                String param = "-F";
                String param1key1 = "chat_id=" + user.getTelegramId();
                String param1key2 = "audio=@" + trackPath;
                String request = "https://api.telegram.org/bot" + botToken + "/sendAudio";
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(
                            command,
                            param, param1key1,
                            param, param1key2,
                            request
                    );
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
                        throw new RuntimeException("Request failed with exit code " + exitCode);
                    }
                    if(!clearDirectory()) {
                        throw new RuntimeException("Failed to clear directory");
                    }
                    return "The track is being sent. :)";
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return e.getMessage();
                }
            } else {
                throw new RuntimeException("Invalid track path");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String downloadTrack(UserDto user, IPlaylistItem item) {
        String command = "yt-dlp";
        String type = "-t";
        String typeName = "mp3";
        String format = "-f";
        String formatType = "bestaudio";
        String pathArg = "-o";
        String path = downloadPath + "/track";
        String link = getYtMusicTrackLink(user, item);
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
                throw new RuntimeException("yt-dlp failed with exit code " + exitCode);
            }
            File[] files = downloadDir.toFile().listFiles((_, name) -> name.endsWith(".mp3"));
            if (files == null || files.length == 0) {
                throw new RuntimeException("No mp3 file was created.");
            }
            File latestFile = files[0];
            for (File f : files) {
                if (f.lastModified() > latestFile.lastModified()) {
                    latestFile = f;
                }
            }
            if (!latestFile.exists()) {
                throw new RuntimeException("File not found after download.");
            }
            return latestFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    private String getYtMusicTrackLink(UserDto user, IPlaylistItem item) {
        if(!spotifyWebApiService.doesTrackExist(user, item)) {
            return "No such track found :(";
        }
        Track track = spotifyWebApiService.getTrackMetadata(user, item);
        String query = track.getName() + " " + track.getArtists()[0].getName();
        String ytMusicTrackId = getYtMusicTrackId(query);
        if(Objects.equals(ytMusicTrackId, "Something went wrong... :(")) {
            return ytMusicTrackId;
        }
        return "https://music.youtube.com/watch?v=" + ytMusicTrackId;
    }

    private String getYtMusicTrackId(String query) {
        try {
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(1L);
            SearchListResponse response = search.execute();
            List<SearchResult> results = response.getItems();
            if (results != null && !results.isEmpty()) {
                return results.getFirst().getId().getVideoId();
            } else {
                throw new IOException("Something went wrong... :(");
            }
        } catch(Exception e) {
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
