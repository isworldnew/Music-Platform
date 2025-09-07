package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.finder.abstraction.TagFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.TagMapper;
import ru.smirnov.musicplatform.repository.domain.finder.TagFinderRepository;

import java.util.List;

@Service
public class TagFinderServiceImplementation implements TagFinderService {

    private final TagFinderRepository tagFinderRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagFinderServiceImplementation(TagFinderRepository tagFinderRepository, TagMapper tagMapper) {
        this.tagFinderRepository = tagFinderRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagResponse> searchTags(String searchRequest, Long userId) {
        List<Tag> tags = this.tagFinderRepository.searchTags(searchRequest, userId);
        return tags.stream().map(tag -> this.tagMapper.tagEntityToTagResponse(tag)).toList();
    }
}
