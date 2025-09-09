package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkRequest;

public interface ArtistSocialNetworkService {

    // Добавление соцсети исполнителю
    Long addSocialNetwork(Long artistId, ArtistSocialNetworkRequest dto, DataForToken tokenData);

    // Обновление ссылки у соцсети исполнителя
    void updateArtistSocialNetworkReference(Long recordId, String reference, DataForToken tokenData);

    // Удаление соцсети исполнителя
    void deleteArtistSocialNetwork(Long recordId, DataForToken tokenData);
}
