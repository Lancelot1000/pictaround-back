package project.pictaround.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Favorite;
import project.pictaround.domain.Member;
import project.pictaround.domain.Review;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@AllArgsConstructor
public class FavoriteRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<Favorite> findByMemberId(Member member) {
        String jpql = "select f from Favorite f where f.member.id = :memberId";

        return em.createQuery(jpql, Favorite.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    public String setFavorite(Review review, Member member) {
        String jpql = "select count(f) from Favorite f where f.review.id = :id and f.member.id = :memberId";

        Long size = em.createQuery(jpql, Long.class)
                .setParameter("id", review.getId())
                .setParameter("memberId", member.getId())
                .getSingleResult();

        if (size > 0) {
            String deleteJpql = "delete from Favorite f where f.review.id = :id and f.member.id = :memberId";
            em.createQuery(deleteJpql)
                    .setParameter("id", review.getId())
                    .setParameter("memberId", member.getId())
                    .executeUpdate();
            return "DELETE";
        } else {
            Favorite favorite = new Favorite(member, review.getLocation(), review, LocalDateTime.now());
            em.persist(favorite);
            return "INSERT";
        }
    }
}
