package ru.smirnov.musicplatform.config;

import lombok.Getter;

@Getter
public enum MinioBuckets {
    
    TRACK_AUDIO("track-audio"),
    TRACK_COVER("track-cover"),
    ARTIST_COVER("artist-cover"),
    ALBUM_COVER("album-cover"),
    PLAYLIST_COVER("playlist-cover"),
    CHART_COVER("chart-cover");
    
    private final String bucketName;

    MinioBuckets(String bucketName) {
        this.bucketName = bucketName;
    }

}
