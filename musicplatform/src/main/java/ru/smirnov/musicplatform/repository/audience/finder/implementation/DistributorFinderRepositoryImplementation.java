package ru.smirnov.musicplatform.repository.audience.finder.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.repository.audience.finder.abstraction.DistributorFinderRepository;

import java.util.List;

@Repository
public class DistributorFinderRepositoryImplementation implements DistributorFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Distributor> searchDistributors(String searchRequest) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Distributor> query = criteriaBuilder.createQuery(Distributor.class);

        Root<Distributor> distributor = query.from(Distributor.class);

        Predicate distributorNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(distributor.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        query.where(distributorNamePredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

}
