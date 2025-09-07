package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.SavedCharts;

@Repository
public interface SavedChartRepository extends JpaRepository<SavedCharts, Long> {

    @Query(
            value = """
                    INSERT INTO saved_charts(user_id, chart_id)
                    VALUES(:userId, :chartId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("userId") Long userId, @Param("chartId") Long chartId);

    @Query(
            value = """
                    DELETE FROM saved_charts
                    WHERE saved_charts.user_id = :userId AND saved_charts.chart_id = :chartId
                    """,
            nativeQuery = true
    )
    void delete(@Param("userId") Long userId, @Param("chartId") Long chartId);

}
