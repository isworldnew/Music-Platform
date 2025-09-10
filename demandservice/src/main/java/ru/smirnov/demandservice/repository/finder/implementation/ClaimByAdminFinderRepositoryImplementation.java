//package ru.smirnov.demandservice.repository.finder.implementation;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Root;
//import org.springframework.stereotype.Repository;
//import ru.smirnov.demandservice.projection.abstraction.AdminClaimProjection;
//import ru.smirnov.demandservice.repository.finder.abstraction.ClaimByAdminFinderRepository;
//
//import java.util.List;
//
//@Repository
//public class ClaimByAdminFinderRepositoryImplementation implements ClaimByAdminFinderRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<AdminClaimProjection> getClaimPerAdminStats() {
//
//        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
//        CriteriaQuery<AdminClaimProjection> query = criteriaBuilder.createQuery(AdminClaimProjection.class);
//
//
//
//    }
//
//}
