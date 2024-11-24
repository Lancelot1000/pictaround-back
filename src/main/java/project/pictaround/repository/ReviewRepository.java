package project.pictaround.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Location;
import project.pictaround.domain.Review;
import project.pictaround.dto.request.FindLocationsRequestDto;
import project.pictaround.dto.request.PageRequestDto;
import project.pictaround.dto.response.LocationDto;

import java.util.List;

@Repository
@Transactional
public class ReviewRepository {

    @PersistenceContext
    EntityManager em;

    public Review save(Review review) {
        em.persist(review);

        return review;
    }

    public long countReviews(Long locationId) {
        String jpql = "SELECT count(r) FROM Review r WHERE r.location.id = :locationId";

        return em.createQuery(jpql, Long.class)
                .setParameter("locationId", locationId)
                .getSingleResult();
    }

    public Review findById(Long id) {
        return em.find(Review.class, id);
    }

    public List<Review> findAllByLocationId(Long locationId, PageRequestDto pageDto) {
        String jpql = "SELECT r FROM Review r WHERE r.location.id = :locationId ORDER BY r.likeCount DESC, r.createdDateTime DESC";

        return em.createQuery(jpql, Review.class)
                .setParameter("locationId", locationId)
                .setFirstResult(pageDto.getOffset())
                .setMaxResults(pageDto.getLimit())
                .getResultList();
    }

    public void updateLikeCount(Review review, int likeCount) {
        String jpql = "UPDATE Review SET likeCount = :count WHERE id = :reviewId";

        em.createQuery(jpql)
                .setParameter("count", likeCount)
                .setParameter("reviewId", review.getId())
                .executeUpdate();
    }
}
