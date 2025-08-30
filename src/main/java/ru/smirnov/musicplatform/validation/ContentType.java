package ru.smirnov.musicplatform.validation;

import lombok.Getter;

import java.util.List;

@Getter
public enum ContentType {

    IMAGE(
            500 * 1024, // 500 килобайт
            List.of(".jpg", ".jpeg", ".png", ".gif", ".svg", ".webp", ".bmp", ".tiff")
    ),
    AUDIO(
            8 * 1024 * 1024, // 8 мегабайт
            List.of(".mp3", ".wav", ".flac", ".aac", ".m4a", ".ogg", ".aiff", ".wma", ".mid", ".midi")
    );

    private final long maxSizeBytes;
    private final List<String> availableTypes;

    ContentType(long maxSizeBytes, List<String> availableTypes) {
        this.maxSizeBytes = maxSizeBytes;
        this.availableTypes = availableTypes;
    }

}
