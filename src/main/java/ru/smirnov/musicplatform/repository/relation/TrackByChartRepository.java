package ru.smirnov.musicplatform.repository.relation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.TracksByCharts;

@Repository
public interface TrackByChartRepository extends JpaRepository<TracksByCharts, Long> {

    @Query(
            value = """
                    INSERT INTO tracks_by_charts(track_id, chart_id)
                    VALUES(:trackId, :chartId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("chartId") Long chartId, @Param("trackId") Long trackId) throws DataIntegrityViolationException;

    @Query(
            value = """
                    DELETE FROM tracks_by_charts
                    WHERE tracks_by_charts.track_id = :trackId AND tracks_by_charts.chart_id = :chartId
                    """,
            nativeQuery = true
    )
    void delete(@Param("chartId") Long chartId, @Param("trackId") Long trackId);
}
