package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.entity.domain.Artist;

import java.util.List;

public interface ArtistFinderRepository {

    List<Artist> searchArtists(String searchRequest);

    List<Artist> searchArtists(String searchRequest, Long distributorId);

    добавь новые методы в сервисы и контроллеры
    и там в каком-то инетерфейсе были описаны нужные методы
}
