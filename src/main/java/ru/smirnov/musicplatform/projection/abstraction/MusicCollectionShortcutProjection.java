package ru.smirnov.musicplatform.projection.abstraction;

import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;

public interface MusicCollectionShortcutProjection {

     Long getId();

     String getName();

     String getImageReference();

     Long getCreatorId(); // id user-а, admin-а или artist-а

     String getCreatorUsername(); // username аккаунта user-а, admin-а или name artist-а

     MusicCollectionAccessLevel getAccessLevel();

     Boolean getSaved();
}
