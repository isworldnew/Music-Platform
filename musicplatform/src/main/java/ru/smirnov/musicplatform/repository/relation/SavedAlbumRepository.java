package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.SavedAlbums;

@Repository
public interface SavedAlbumRepository extends JpaRepository<SavedAlbums, Long> {

    @Query(
            value = """
                    INSERT INTO saved_albums(user_id, album_id)
                    VALUES(:userId, :albumId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("userId") Long userId, @Param("albumId") Long albumId);

    @Query(
            value = """
                    DELETE FROM saved_albums
                    WHERE saved_albums.user_id = :userId AND saved_albums.album_id = :albumId
                    """,
            nativeQuery = true
    )
    @Modifying
    void delete(@Param("userId") Long userId, @Param("albumId") Long albumId);

}
