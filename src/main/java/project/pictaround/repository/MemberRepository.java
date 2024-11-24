package project.pictaround.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pictaround.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member save(Member member);

    boolean existsByLoginId(String loginId);

    Member findByLoginId(String loginId);
    Optional<Member> findById(Long id);

}
