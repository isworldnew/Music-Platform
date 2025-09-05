package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.entity.domain.Tag;

import java.util.List;

public interface TagFinderRepository {

    List<Tag> searchTags(String searchRequest, Long userId);

}
