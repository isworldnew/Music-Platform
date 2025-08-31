package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.ExtendedTrackResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;

import java.util.List;


@Service
public class TrackServiceImplementation implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackPreconditionService trackPreconditionService;
    private final TrackMapper trackMapper;

    private final ArtistPreconditionService artistPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public TrackServiceImplementation(
            TrackRepository trackRepository,
            TrackPreconditionService trackPreconditionService,
            TrackMapper trackMapper,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.trackRepository = trackRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.trackMapper = trackMapper;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional
    public Long uploadTrack(Long artistId, TrackRequest dto, DataForToken tokenData) {
        /*
        Теперь трек создаётся "в сыром виде":
        - без обложки и аудиофайла
        - без указания соавторов
        Всё это будет действиями редактирования трека (+ не всё останется в этом сервисе)

        Проверки:
        [v] Переданный исполнитель существует
        [v] Дистрибьютор, сделавший запрос, имеет ACTIVE-связь с данным исполнителем
        [v] У данного исполнителя ещё нет треков с таким названием
        */
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artist.getId());
        this.trackPreconditionService.existsByName(artist.getId(), dto.getName());

        Track track = this.trackMapper.trackRequestToTrackEntity(dto, artist);
        this.trackRepository.save(track);

        return track.getId();
    }

    @Override
    @Transactional
    public void updateTrack(Long trackId, TrackRequest dto, DataForToken tokenData) {
        /*
        Проверки:
        [v] Трек по переданному id существует
        При обновлении трека может прийти его старое имя, а может - новое
        [v] Проверяю, принадлежит ли переданное имя найденному треку (если принадлежит - возвращаю)
        [v] Если не принадлежит - ищу у данного исполнителя треки с таким названием (если нашёлся - кидаю исключение)
        [v] Дистрибьютор, сделавший запрос, имеет ACTIVE-связь с исполнителем трека
        */
        Track track = this.trackPreconditionService.getByIdIfExistsAndNameIsUniquePerArtist(trackId, dto.getName());
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());

        track.setName(dto.getName());
        track.setGenre(dto.getGenre());

        this.trackRepository.save(track);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateTrackAccessLevel(Long trackId, TrackAccessLevelRequest dto, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        if (!dto.getAccessLevel().equals(TrackStatus.PUBLISHED.name()) && track.getAudiofileReference() == null)
            throw new ForbiddenException("Track (id=" + trackId + ") can't be PUBLISHED because it has no audiofile uploaded");

        if (tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {
            this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());

            if (track.getStatus().equals(TrackStatus.BANNED))
                throw new ForbiddenException("Track (id=" + trackId + ") is BANNED, only ADMIN has rights to change this status");

            if (dto.getAccessLevel().equals(TrackStatus.BANNED.name()))
                throw new ForbiddenException("Only ADMIN has rights to BAN the track (self-ban is not allowed to distributor)");
        }

        track.setStatus(TrackStatus.valueOf(dto.getAccessLevel()));
        this.trackRepository.save(track);
    }

    @Override
    @Transactional
    public void listenToTrack(Long trackId) {
        Track track = this.trackPreconditionService.getIfExistsAndPublic(trackId);
        track.setNumberOfPlays(track.getNumberOfPlays() + 1);
        this.trackRepository.save(track);
    }

    /*
    где, когда и кому может понадобиться трек...

    Трек можно просматривать полноценно, а можно как shortcut в какой-то музыкальной коллекции или списке сохранённых треков

    ТЕГИ!!!

    Полноценный просмотр:
    - ADMIN и DISTRIBUTOR: получить всю информацию о треке вне зависимости от его статуса (+ соавторы)

    - USER: вообще, нужно сделать правило для юзера, что трек должен быть сохранён для добавления его в плейлист или для пометки тегом
    -- если трек не сохранён и не PUBLIC, то просто FORBIDDEN
    -- если трек не сохранён и PUBLIC, то полная информация, но без тегов
    -- если трек сохранён и не PUBLIC, то информация с тегами, но со скрытыми ссылками
    -- если трек сохранён и PUBLIC, то полная информация вместе с тегами

    - GUEST:
    -- если трек не PUBLIC, то просто FORBIDDEN
    -- если трек PUBLIC, то полная информация, но без тегов

    Шорткаты (в коллекциях или в списке сохранённых):
    с ними посложнее, потому что они будут участовать в поиске и в списках сохранённых / коллекций

    */

    @Override
    public TrackResponse getTrackByIdWithNoRestrictions(Long trackId, DataForToken tokenData) {
        // для ADMIN и DISTRIBUTOR
        // так... а дистрибьютор может просматривать вообще все треки? Просто, как будто, только своего исполнителя (ещё и статус надо учесть (опять наверное ACTIVE))
        // и если его исполнитель есть в соавторстве у данного трека
        // а вот админ может просмотреть любой трек

        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        /*
        Дистрибьютор в целом не имеет влияения на трек, если исполнитель, с которым у данного дистрибьютора есть ACTIVE-связь,
        указан в данном треке просто как соавтор... (разве что может добавить такой трек в альбом или удалить оттуда) Соответственно,
        и смысла давать возможность просматривать чужой трек, где твой ACTIVE-исполнитель является соавтором - нет

        или наоборот - смысл есть? Мол, тебе же надо видеть, какой трек ты в альбом добавляешь
        */
        /*
        Нужно взять список ACTIVE-исполнителей обратившегося дистрибьютора, а затем посмортеть, есть ли кто-то из них
        среди соавторов запрашиваемого трека
        */
        if (tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {
            List<Long> coArtists = track.getCoArtists().stream().map(coArtist -> coArtist.getArtist().getId()).toList();

            // тогда нужен ещё метод просмотра всех соавторств у своих исполнителей
        }

        return this.trackMapper.trackEntityToTrackResponse(track);
    }

    @Override
    public TrackResponse getTrackById(Long trackId) {
        // для GUEST
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        if (!track.getStatus().isAvailable())
            throw new ForbiddenException("Track (id=" + trackId + ") is unavailable (non-PUBLIC status)");

        return this.trackMapper.trackEntityToTrackResponse(track);
    }

    @Override
    public ExtendedTrackResponse getTrackWithPossibleRestrictions(Long trackId, DataForToken tokenData) {
        // для USER
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        // если трек не сохранён и не-PUBLIC -> Forbidden

        // если трек не сохранён и PUBLIC -> TrackResponse

        // если трек сохранён и не-PUBLIC -> ExtendedTrackResponse, но со скрытыми ссылками на обложку и аудио

        // если трек сохранён и PUBLIC -> ExtendedTrackResponse

    }

    // а выбор всех:
    // сохраннёных треков?
    // тегнутых треков?
    // треков в плейлисте (своём / чужом)
    // треков в альбоме (сохранённом / или для дистрибьютора)
    // треков в чарте (сохранённом / или для админа)
    // ?
    // ну это больше к шорткатам...


}
