package ru.smirnov.musicplatform.repository.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Playlist;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByNameAndUserId(String name, Long userId);

    List<Playlist> findAllByUserId(Long userId);

}
