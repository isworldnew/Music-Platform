package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.tag.TagRequest;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.abstraction.TagMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TagPreconditionService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.TagRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.TagService;

import java.util.List;

// [v] checked
@Service
public class TagServiceImplementation implements TagService {

    private final TagRepository tagRepository;
    private final TagPreconditionService tagPreconditionService;
    private final TagMapper tagMapper;

    private final UserRepository userRepository;

    @Autowired
    public TagServiceImplementation(
            TagRepository tagRepository,
            TagPreconditionService tagPreconditionService,
            TagMapper tagMapper,
            UserRepository userRepository
    ) {
        this.tagRepository = tagRepository;
        this.tagPreconditionService = tagPreconditionService;
        this.tagMapper = tagMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Long createTag(TagRequest dto, DataForToken tokenData) {
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        this.tagPreconditionService.existsByNameAndUserId(dto.getName(), user.getId());

        Tag tag = this.tagMapper.tagRequestToTagEntity(dto, user);
        this.tagRepository.save(tag);

        return tag.getId();
    }

    @Override
    public TagResponse getTagById(Long tagId, DataForToken tokenData) {
        Tag tag = this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, tokenData.getEntityId());
        return this.tagMapper.tagEntityToTagResponse(tag);
    }

    @Override
    public List<TagResponse> getAllUserTags(DataForToken tokenData) {
        return this.tagRepository.findAllByUserId(tokenData.getEntityId()).stream()
                .map(tag -> this.tagMapper.tagEntityToTagResponse(tag))
                .toList();
    }

    @Override
    @Transactional
    public void updateTag(Long tagId, TagRequest dto, DataForToken tokenData) {
        /*
        это по сути провека на то, что имя передаётся только новое
        она не даст задать тегу его старое имя без изменений
        */
        this.tagPreconditionService.existsByNameAndUserId(dto.getName(), tokenData.getEntityId());

        Tag tag = this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, tokenData.getEntityId());

        tag.setName(dto.getName());
        this.tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId, DataForToken tokenData) {
        Tag tag = this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, tokenData.getEntityId());

        // связанные с данным tag.id записи в tagged_tracks (где такой же tag_id) - тоже удалятся
        this.tagRepository.deleteById(tagId);
    }
}
