package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.auxiliary.hierarchy.OfficialMusicCollection;
import ru.smirnov.musicplatform.entity.relation.SavedCharts;
import ru.smirnov.musicplatform.entity.relation.TracksByCharts;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "charts")
@Data
public class Chart extends OfficialMusicCollection {

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference
    private Admin admin;

    @OneToMany(mappedBy = "chart")
    @JsonManagedReference
    private List<TracksByCharts> tracks = new ArrayList<>();

    @OneToMany(mappedBy = "chart")
    @JsonManagedReference
    private List<SavedCharts> savedBy = new ArrayList<>();

}
