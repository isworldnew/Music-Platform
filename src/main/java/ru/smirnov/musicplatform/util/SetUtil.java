package ru.smirnov.musicplatform.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {

    // эта утилита нужна для работы с множествами

    // предположим: в альбоме было множество треков
    // а пользователь прислал новое множество треков (ну где-то в UI он составил новое множество треков, относящихся к альбому)
    // нужно понять:
    // какие треки добавлены (чтобы их добавить);
    // какие треки удалены (чтобы их удалить);

    public static <T> Set<T> findAddedValues(Set<T> valuesBefore, Set<T> valuesAfter) {
        Set<T> addedValues = new HashSet<>(valuesAfter);
        addedValues.removeAll(valuesBefore);
        return addedValues;
    }

    public static <T> Set<T> findRemovedValues(Set<T> valuesBefore, Set<T> valuesAfter) {
        Set<T> removedValues = new HashSet<>(valuesBefore);
        removedValues.removeAll(valuesAfter);
        return removedValues;
    }

}
