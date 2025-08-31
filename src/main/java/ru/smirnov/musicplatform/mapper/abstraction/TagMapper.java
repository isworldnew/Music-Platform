package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.tag.TagRequest;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Tag;

public interface TagMapper {

    Tag tagRequestToTagEntity(TagRequest dto, User user);

    TagResponse tagEntityToTagResponse(Tag tag);
}
