package org.raghoul.raghoulwavebot.service.spotifywebapi;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.raghoul.raghoulwavebot.dto.spotifyresponse.SpotifyResponseDTO;
import org.raghoul.raghoulwavebot.dto.user.UserDto;
import org.raghoul.raghoulwavebot.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Getter
@Setter
@ToString
@Component
public class SpotifyWebApiAuthorizationServiceImpl implements SpotifyWebApiAuthorizationService {

    private final UserService userService;

    private String clientId;
    private String clientSecret;
    private String redirectUriString;
    private URI redirectUri;
    private SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;
    private AuthorizationCodeRequest authorizationCodeRequest;

    @Autowired
    public SpotifyWebApiAuthorizationServiceImpl(
            UserService userService, String clientId,
            String clientSecret,
            String redirectUriString
    ) {
        this.userService = userService;

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
    public void authorizationCode_Sync(SpotifyResponseDTO spotifyResponseDTO) {

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

        UserDto userDto = userService.getByState(state);
        userDto.setRefreshToken(refreshToken);
        userService.update(userDto);
    }

    @Override
    public String authorizationCode_Async() {
        return "";
    }

    @Override
    public String authorizationCodeRefresh_Sync() {
        return "";
    }

    @Override
    public String authorizationCodeRefresh_Async() {
        return "";
    }
}
