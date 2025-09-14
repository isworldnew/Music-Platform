package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.TaggedTracks;

@Repository
public interface TaggedTrackRepository extends JpaRepository<TaggedTracks, Long> {

    @Query(
            value = """
                    INSERT INTO tagged_tracks(track_id, tag_id)
                    VALUES(:trackId, :tagId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("trackId") Long trackId, @Param("tagId") Long tagId);

    @Query(
            value = """
                    DELETE FROM tagged_tracks
                    WHERE tagged_tracks.track_id = :trackId AND tagged_tracks.tag_id = :tagId
                    """,
            nativeQuery = true
    )
    @Modifying
    void delete(@Param("trackId") Long trackId, @Param("tagId") Long tagId);

}
