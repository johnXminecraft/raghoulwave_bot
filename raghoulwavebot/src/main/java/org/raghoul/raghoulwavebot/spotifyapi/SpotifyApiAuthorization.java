package org.raghoul.raghoulwavebot.spotifyapi;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Getter
@Setter
@ToString
@Component
public class SpotifyApiAuthorization {

    private String clientId;
    private String clientSecret;
    private String redirectUriString;
    private URI redirectUri;
    private SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    @Autowired
    public SpotifyApiAuthorization(
            String clientId,
            String clientSecret,
            String redirectUriString
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUriString = redirectUriString;
        this.redirectUri = SpotifyHttpManager.makeUri(redirectUriString);

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .response_type("code")
                .client_id(clientId)
                .redirect_uri(redirectUri)
                .state(RandomStringUtils.random(16, true, false))
                .build();
    }

    public String authorizationCodeUri_Sync() {

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();
    }

    public String authorizationCodeUri_Async() {
        try {

            final CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();

            final URI uri = uriFuture.join();

            return uri.toString();
        } catch (CompletionException e) {

            System.out.println("Error: " + e.getCause().getMessage());

            return null;
        } catch (CancellationException e) {

            System.out.println("Async operation cancelled.");

            return null;
        }
    }
}
