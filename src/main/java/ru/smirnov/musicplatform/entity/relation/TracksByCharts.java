package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.entity.domain.Track;

@Entity
@Table(
        name = "tracks_by_charts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"track_id", "chart_id"})
)
@Data
public class TracksByCharts {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonBackReference
    private Track track;

    @ManyToOne
    @JoinColumn(name = "chart_id")
    @JsonBackReference
    private Chart chart;

}
