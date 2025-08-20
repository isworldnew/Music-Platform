package ru.smirnov.musicplatform.authentication;


public enum JwtToken {

    ACCESS_TOKEN(30 * 60 * 1000), // 30 минут
    REFRESH_TOKEN(7 * 24 * 60 * 60 * 1000); // 7 дней

    private final long validity;

    JwtToken(long validity) {
        this.validity = validity;
    }

    public long validityDuration() {
        return this.validity;
    };

}