package ru.smirnov.musicplatform.entity.audience;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.entity.domain.Chart;

import java.util.ArrayList;
import java.util.List;

@Entity // в SpEL будет имя самого класса или значение, заданное в @Entity?
@Table(name = "admins")
@Data
public class Admin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Embedded
    private CommonPersonData data;

    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private List<Chart> charts = new ArrayList<>();

}
