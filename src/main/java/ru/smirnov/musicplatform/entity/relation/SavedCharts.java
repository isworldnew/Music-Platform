package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Chart;

@Entity
@Table(name = "saved_charts")
@Data
public class SavedCharts {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chart_id")
    @JsonBackReference
    private Chart chart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
