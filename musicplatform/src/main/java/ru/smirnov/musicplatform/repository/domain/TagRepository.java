package ru.smirnov.musicplatform.repository.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameAndUserId(String name, Long userId);

    List<Tag> findAllByUserId(Long userId);
}
