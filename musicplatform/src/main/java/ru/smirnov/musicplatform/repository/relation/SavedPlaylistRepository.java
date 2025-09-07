package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.SavedPlaylists;

@Repository
public interface SavedPlaylistRepository extends JpaRepository<SavedPlaylists, Long> {

    @Query(
            value = """
                    INSERT INTO saved_playlists(user_id, playlist_id)
                    VALUES(:userId, :playlistId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("userId") Long userId, @Param("playlistId") Long playlistId);

    @Query(
            value = """
                    DELETE FROM saved_playlists
                    WHERE saved_playlists.user_id = :userId AND saved_playlists.playlist_id = :playlistId
                    """,
            nativeQuery = true
    )
    void delete(@Param("userId") Long userId, @Param("playlistId") Long playlistId);
}
