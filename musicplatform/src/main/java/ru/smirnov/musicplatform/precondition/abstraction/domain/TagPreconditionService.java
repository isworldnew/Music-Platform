package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Tag;

public interface TagPreconditionService {

    void existsByNameAndUserId(String name, Long userId);

    Tag getByIdIfExistsAndBelongsToUser(Long tagId, Long userId);

}
