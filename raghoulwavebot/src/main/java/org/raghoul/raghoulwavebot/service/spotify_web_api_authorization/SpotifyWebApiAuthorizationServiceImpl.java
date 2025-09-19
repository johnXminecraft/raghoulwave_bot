package org.raghoul.raghoulwavebot.service.spotify_web_api_authorization;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.spotify_authorization_response.SpotifyAuthorizationResponseDto;
import org.raghoul.raghoulwavebot.dto.bot_user.BotUserDto;
import org.raghoul.raghoulwavebot.service.bot_user.BotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Getter
@Setter
@ToString
@Service
public class SpotifyWebApiAuthorizationServiceImpl implements SpotifyWebApiAuthorizationService {

    /*TODO
    *  Async methods
    *  Custom exceptions for try-catch checks
    *  Perhaps some cosmetic BS
    *  Fix Constructor Ig */

    private final BotUserService botUserService;
    private String clientId;
    private String clientSecret;
    private String redirectUriString;
    private URI redirectUri;
    private SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;
    private AuthorizationCodeRequest authorizationCodeRequest;
    private AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;

    @Autowired
    public SpotifyWebApiAuthorizationServiceImpl(
            BotUserService botUserService, String clientId,
            String clientSecret,
            String redirectUriString
    ) {
        this.botUserService = botUserService;

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUriString = redirectUriString;

        this.redirectUri = SpotifyHttpManager.makeUri(redirectUriString);

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }

    @Override
    public String authorizationCodeUri_Sync(String state) {

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .response_type("code")
                .client_id(clientId)
                .redirect_uri(redirectUri)
                .state(state)
                .scope("user-read-recently-played,user-library-read,user-read-currently-playing")
                .build();

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();
    }

    @Override
    public String authorizationCodeUri_Async(String state) {

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .response_type("code")
                .client_id(clientId)
                .redirect_uri(redirectUri)
                .state(state)
                .build();

        try {

            final CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();

            final URI uri = uriFuture.join();

            return uri.toString();
        } catch (CompletionException e) {

            System.out.println("Error: " + e.getCause().getMessage());

            return "But we are having some problems on our side, please try again later.";
        } catch (CancellationException e) {

            System.out.println("Async operation cancelled.");

            return "But we are having some problems on our side, please try again later.";
        }
    }

    @Override
    public void authorizationCode_Sync(SpotifyAuthorizationResponseDto spotifyResponseDTO) {

        String refreshToken = "IRT";
        String state = spotifyResponseDTO.getState();

        authorizationCodeRequest = spotifyApi.authorizationCode(spotifyResponseDTO.getCode())
                .build();

        try {

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            refreshToken = authorizationCodeCredentials.getRefreshToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {

            System.out.println("Error: " + e.getMessage());
        }

        BotUserDto userDto = botUserService.getByState(state);
        userDto.setRefreshToken(refreshToken);
        userDto.setBotState("ready");
        botUserService.update(userDto);
    }

    @Override
    public String authorizationCode_Async() {
        return "";
    }

    @Override
    public String authorizationCodeRefresh_Sync(BotUserDto botUserDto) {

        if(!Objects.equals(botUserDto.getRefreshToken(), "IRT")) {
            spotifyApi.setRefreshToken(botUserDto.getRefreshToken());
        } else {
            return "CNGRT";
        }

        authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            return authorizationCodeCredentials.getAccessToken();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.out.println(e.getMessage());

            return "CNGAT";
        }
    }

    @Override
    public String authorizationCodeRefresh_Async() {
        return "";
    }
}
