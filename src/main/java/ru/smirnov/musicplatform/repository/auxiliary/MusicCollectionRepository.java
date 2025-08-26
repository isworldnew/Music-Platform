package ru.smirnov.musicplatform.repository.auxiliary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

// нужен для обобщения работы создателей музыкальных коллекций и с конкретным типом коллекции
// user -> playlist
// distributor -> album
// admin -> chart
@NoRepositoryBean
public interface MusicCollectionRepository<T, ID> extends JpaRepository<T, ID> {

    @Query()
    T save();

}
