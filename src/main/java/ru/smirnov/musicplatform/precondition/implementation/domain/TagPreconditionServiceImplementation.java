package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TagPreconditionService;
import ru.smirnov.musicplatform.repository.domain.TagRepository;

import java.util.List;

@Service
public class TagPreconditionServiceImplementation implements TagPreconditionService {

    private final TagRepository tagRepository;

    @Autowired
    public TagPreconditionServiceImplementation(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void existsByNameAndUserId(String name, Long userId) {
        Tag tag = this.tagRepository.findByNameAndUserId(name, userId).orElse(null);

        if (tag != null)
            throw new ConflictException("Name '" + name + "' belongs to user's (id=" + userId + ") tag (id=" + tag.getId() + ")");
    }

    @Override
    public Tag getByIdIfExistsAndBelongsToUser(Long tagId, Long userId) {
        Tag tag = this.getByIdIfExists(tagId);

        if (!tag.getId().equals(userId))
            throw new ForbiddenException("Tag (id=" + tagId + ") doesn't belong to user (id=" + userId + ")");

        return tag;
    }

    private Tag getByIdIfExists(Long tagId) {
        return this.tagRepository.findById(tagId).orElseThrow(
                () -> new NotFoundException("Tag with id=" + tagId + " was not found")
        );
    }
}
