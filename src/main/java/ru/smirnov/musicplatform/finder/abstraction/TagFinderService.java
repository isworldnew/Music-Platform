package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;

import java.util.List;

public interface TagFinderService {

    List<TagResponse> searchTags(String searchRequest, Long userId);

}
