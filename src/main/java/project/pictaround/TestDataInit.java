package project.pictaround;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import project.pictaround.domain.Category;
import project.pictaround.domain.Location;
import project.pictaround.domain.Member;
import project.pictaround.domain.Review;
import project.pictaround.dto.request.JoinRequestDto;
import project.pictaround.repository.CategoryRepository;
import project.pictaround.repository.LocationRepository;
import project.pictaround.repository.MemberRepository;
import project.pictaround.repository.ReviewRepository;
import project.pictaround.service.LoginService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class TestDataInit {

    private CategoryRepository categoryRepository;
    private MemberRepository memberRepository;
    private ReviewRepository reviewRepository;
    private LocationRepository locationRepository;


    /**
     * 확인용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        // 카테고리
        categoryRepository.save(new Category("all", "전체"));
        categoryRepository.save(new Category("food", "식사"));
        categoryRepository.save(new Category("bakery", "베이커리"));
        categoryRepository.save(new Category("outing", "나들이"));
        categoryRepository.save(new Category("experience", "체험"));
        categoryRepository.save(new Category("exhibition", "전시"));
        Category etcCategory = categoryRepository.save(new Category("etc", "etc"));

        // 회원가입
        Member member = memberRepository.save(new Member("test", "닉네임", "password"));

        // 장소 등록
        Location location1 = locationRepository.save(new Location(37.3131550F, 126.8311518F, "NC 백화점", "NC 백화점 주소", etcCategory));
        Location location2 = locationRepository.save(new Location(37.3119431F, 126.8289038F, "안산 홈플러스", "안산 홈플러스 주소", etcCategory));
        Location location3 = locationRepository.save(new Location(37.3111480F, 126.8306362F, "안산 포크너", "안산 포크너 주소", etcCategory));

        // 리뷰 등록
        Review review1 = reviewRepository.save(new Review("NC 백화점 코멘트1", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location1));
        Review review2 = reviewRepository.save(new Review("NC 백화점 코멘트2", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location1));
        Review review3 = reviewRepository.save(new Review("안산 홈플러스 코멘트1", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location2));
        Review review4 = reviewRepository.save(new Review("안산 홈플러스 코멘트2", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location2));
        Review review5 = reviewRepository.save(new Review("안산 포크너 코멘트1", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location3));
        Review review6 = reviewRepository.save(new Review("안산 포크너 코멘트2", "http://picsum.photos/200/300", LocalDateTime.now(), 0, member, location3));

        locationRepository.updateBestReviewId(location1, review1);
        locationRepository.updateBestReviewId(location2, review3);
        locationRepository.updateBestReviewId(location3, review5);
    }
}
