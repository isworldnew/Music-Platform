package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.TagRequest;
import ru.smirnov.musicplatform.dto.tmp.TagResponse;

import java.util.List;

public interface TagService {

    Long createTag(TagRequest dto, DataForToken tokenData);

    TagResponse getTagById(Long tagId, DataForToken tokenData);

    List<TagResponse> getAllTags(DataForToken tokenData);

    void updateTag(Long tagId, TagRequest dto, DataForToken tokenData);

    void deleteTag(Long tagId, DataForToken tokenData);

}
