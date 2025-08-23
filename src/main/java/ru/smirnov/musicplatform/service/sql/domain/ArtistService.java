package ru.smirnov.musicplatform.service.sql.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.dto.ArtistToCreateDto;
import ru.smirnov.musicplatform.exception.ArtistNameNonUniqueException;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.DistributorByArtistService;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final DistributorByArtistService distributorByArtistService;
    private final MinioService minioService;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, DistributorByArtistService distributorByArtistService, MinioService minioService) {
        this.artistRepository = artistRepository;
        this.distributorByArtistService = distributorByArtistService;
        this.minioService = minioService;
    }

    // транзакция
    public ResponseEntity<Long> createArtist(ArtistToCreateDto dto) {

        if (this.artistRepository.findByName(dto.getName()).isPresent())
            throw new ArtistNameNonUniqueException("Such artist's name already exists");

        // проверить обложку: объём, (не)пустота

        // создать запись об Исполнителе

        // на основе этой записи создать связь в расшивающей таблице

        // так как связь Исполнителей с расшивающей таблицей - 1:1, то наверное вернуть достаточно только id Исполнителя

        // создать соцсети

    }

}
