package project.pictaround.repository;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Location;
import project.pictaround.domain.Review;
import project.pictaround.dto.request.FindLocationsRequestDto;
import project.pictaround.dto.response.KeywordResponseDto;
import project.pictaround.dto.response.LocationDto;

import java.util.List;

@Slf4j
@Repository
@Transactional
public class LocationRepository {

    @PersistenceContext
    private final EntityManager em;

    public LocationRepository(EntityManager em) {
        this.em = em;
    }

    public Location save(Location location) {
        em.persist(location);
        return location;
    }

    public void updateBestReviewId(Location location, Review review) {
        String jpql = "update Location l set l.bestReviewId = :bestReviewId where l.id = :locationId";

        em.createQuery(jpql)
                .setParameter("bestReviewId", review.getId())
                .setParameter("locationId", location.getId())
                .executeUpdate();
    }

    public Location findByLocationId(Long locationId) {
        String jpql = "select l from Location l where l.id = :locationId";

        List<Location> locations = em.createQuery(jpql, Location.class)
                .setParameter("locationId", locationId)
                .getResultList();

        return locations.isEmpty() ? null : locations.get(0);
    }

    public List<Location> findByRange(FindLocationsRequestDto requestDto) {
        String jpql = "select l from Location l where l.latitude >= :minLat and l.latitude <= :maxLat and l.longitude >= :minLng and l.longitude <= :maxLng";

        if (!requestDto.getCategory().equals("all")) {
            jpql += " and l.category.name = :category";
        }

        TypedQuery<Location> query = em.createQuery(jpql, Location.class)
                .setParameter("minLat", requestDto.getMinLat())
                .setParameter("maxLat", requestDto.getMaxLat())
                .setParameter("minLng", requestDto.getMinLng())
                .setParameter("maxLng", requestDto.getMaxLng())
                .setFirstResult(requestDto.getOffset())
                .setMaxResults(requestDto.getLimit());

        if (!requestDto.getCategory().equals("all")) {
            query.setParameter("category", requestDto.getCategory());
        }

        return query.getResultList();
    }

    public long countByCondition(FindLocationsRequestDto requestDto) {
        String jpql = "select count(l) from Location l where l.latitude >= :minLat and l.latitude <= :maxLat and l.longitude >= :minLng and l.longitude <= :maxLng";

        if (!requestDto.getCategory().equals("all")) {
            jpql += " and l.category.name = :category";
        }

        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("minLat", requestDto.getMinLat())
                .setParameter("maxLat", requestDto.getMaxLat())
                .setParameter("minLng", requestDto.getMinLng())
                .setParameter("maxLng", requestDto.getMaxLng());

        if (!requestDto.getCategory().equals("all")) {
            query.setParameter("category", requestDto.getCategory());
        }

        return query.getSingleResult();
    }

    public List<LocationDto> findByLikeCountDesc(FindLocationsRequestDto requestDto, List<Location> locations) {
        List<Long> locationIds = locations.stream().map(Location::getId).toList();

        String nativeQuery = "SELECT sub.location_id as location_id, sub.category_id as categoryId, sub.name as name, sub.latitude as latitude, sub.longitude as longitude, r.image_location as imageLocation " +
                "FROM review r " +
                "INNER JOIN (" +
                "SELECT * " +
                "FROM location l " +
                "WHERE l.location_id in (:locationIds) " +
                ") sub " +
                "ON sub.best_review_id = r.review_id " +
                "ORDER BY r.like_count DESC";

        List<LocationDto> results = em.createNativeQuery(nativeQuery, LocationDto.class)
                .setParameter("locationIds", locationIds)
                .setMaxResults(requestDto.getLimit())
                .setFirstResult(requestDto.getOffset())
                .getResultList();

        return results;
    }

    public Location isExistLocation(KeywordResponseDto requestDto) {
        String jpql = "select l from Location l where l.latitude = :latitude and l.longitude = :longitude and l.name = :name";

        List<Location> result = em.createQuery(jpql, Location.class)
                .setParameter("latitude", requestDto.getLatitude())
                .setParameter("longitude", requestDto.getLongitude())
                .setParameter("name", requestDto.getTitle())
                .getResultList();

        if (result.isEmpty()) return null;

        return result.get(0);
    }


}
