package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.repository.domain.finder.TagFinderRepository;

import java.util.List;

@Repository
public class TagFinderRepositoryImplementation implements TagFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> searchTags(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> tag = query.from(Tag.class);

        Predicate tagByUserPredicate = criteriaBuilder.equal(
                tag.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(tag.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        query.where(criteriaBuilder.and(tagByUserPredicate, namePredicate));

        return this.entityManager.createQuery(query).getResultList();
    }
}
