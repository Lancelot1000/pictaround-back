package project.pictaround.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pictaround.domain.Session;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    Session findBySessionValue(String sessionValue);

    List<Session> findByUserId(Long userId);

    void deleteBySessionValue(String sessionValue);

}
