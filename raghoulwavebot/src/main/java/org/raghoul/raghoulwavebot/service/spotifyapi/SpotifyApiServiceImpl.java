package org.raghoul.raghoulwavebot.service.spotifyapi;

import se.michaelthelin.spotify.SpotifyApi;

public class SpotifyApiServiceImpl implements SpotifyApiService{
    SpotifyApi spotifyApi = SpotifyApi.builder().build();
}
