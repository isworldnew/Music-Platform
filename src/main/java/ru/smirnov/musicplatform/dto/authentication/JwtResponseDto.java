package ru.smirnov.musicplatform.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponseDto {

    private final String accessToken;

    private final String refreshToken;

}
