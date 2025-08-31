package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.tag.TagRequest;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.mapper.abstraction.TagMapper;

@Component
public class TagMapperImplementation implements TagMapper {

    @Override
    public Tag tagRequestToTagEntity(TagRequest dto, User user) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setUser(user);
        return tag;
    }

    @Override
    public TagResponse tagEntityToTagResponse(Tag tag) {
        TagResponse dto = new TagResponse();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
