package ru.smirnov.musicplatform.dto.domain.musiccollection;

import lombok.Data;
import ru.smirnov.musicplatform.dto.Available;
import ru.smirnov.musicplatform.dto.Saveable;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;

@Data
public class MusicCollectionShortcutResponse implements Available, Saveable {

    private Long id;

    private Long name;

    private String coverReference;

    private MusicCollectionAccessLevel accessLevel;

    // Boolean, потому что для GUEST это поле будет null
    private Boolean isSaved;

    private MusicCollectionOwnerResponse owner;

    @Override
    public boolean isAvailable() {
        return this.accessLevel.isAvailable();
    }

    @Override
    public boolean isSaved() {
        return (this.isSaved!=null && this.isSaved);
    }
}
