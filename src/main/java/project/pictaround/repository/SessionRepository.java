package project.pictaround.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Session;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    Session findBySessionValue(String sessionValue);

    List<Session> findByUserId(Long userId);


    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM Session s " +
            "WHERE s.sessionValue = :sessionValue")
    void deleteBySessionValue(@Param("sessionValue") String sessionValue);

    @Modifying
    @Transactional
    @Query("UPDATE Session s " +
            "SET s.expiresAt = :expiresAt, s.sessionValue = :sessionValue " +
            "WHERE s.id = :sessionId")
    void updateSessionBy(
            @Param("expiresAt") LocalDateTime expiresAt,
            @Param("sessionValue") String sessionValue,
            @Param("sessionId") Long sessionId
    );

//    @Modifying
//    @Transactional
//    @Query("DELETE " +
//            "FROM Session s " +
//            "WHERE s.sessionValue = :sessionValue")
//    void deleteBySessionValue(
//            @Param("sessionValue") String sessionValue
//    );
}
