package project.pictaround.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import project.pictaround.config.SearchProperties;
import project.pictaround.domain.Category;
import project.pictaround.domain.Location;
import project.pictaround.domain.Member;
import project.pictaround.domain.Review;
import project.pictaround.dto.request.CreateReviewRequestDto;
import project.pictaround.dto.request.FindLocationsRequestDto;
import project.pictaround.dto.request.PageRequestDto;
import project.pictaround.dto.response.*;
import project.pictaround.exception.NotFoundException;
import project.pictaround.exception.UnauthorizedException;
import project.pictaround.repository.CategoryRepository;
import project.pictaround.repository.LocationRepository;
import project.pictaround.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    ReviewRepository reviewRepository;
    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    SessionService sessionService;
    SearchProperties searchProperties;

    @Transactional
    public void createReview(
            CreateReviewRequestDto reviewRequestDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Member member;
        Category category;
        Location location;

        boolean needCreateLocation = false;

        // 멤버 확인
        member = sessionService.findMember(request, response, true);

        // 카테고리 확인
        category = categoryRepository.findById(reviewRequestDto.getCategoryId());

        // 장소 확인
        location = locationRepository.isExistLocation(reviewRequestDto.getLocation());

        // 장소 O + 비로그인 -> 에러
        log.info("location = {}", location);
        log.info("location = {}", location != null);
        if (location != null && member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        // 장소가 없으면 새로 생성
        if (location == null) {
            needCreateLocation = true;
            location = createLocation(CreateReviewRequestDto.toLocationEntity(reviewRequestDto, category));
        }

        Review review = reviewRepository.save(CreateReviewRequestDto.toEntity(reviewRequestDto, member, location));

        // 새로생긴 장소이므로, 첫 리뷰 글이 best 사진이 됨
        if (needCreateLocation == true) {
            locationRepository.updateBestReviewId(location, review);
            location.setBestReviewId(review.getId());

        }

        review.setLocation(location);
    }


    @Transactional
    public Location createLocation(Location location) {
        locationRepository.save(location);

        return location;
    }

    public List<KeywordResponseDto> findSearchKeyword(String query) {
        List<KeywordResponseDto> keywords = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            return keywords;
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(searchProperties.getHost()) // 기본 URL 설정
                .defaultHeader("X-Naver-Client-Id", searchProperties.getId()) // 헤더 설정
                .defaultHeader("X-Naver-Client-Secret", searchProperties.getSecret())
                .build();

        List<KeywordItemDto> items = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("display", 5)
                        .queryParam("start", 1)
                        .queryParam("sort", "random") // 정확도순 내림차순
                        .build())
                .retrieve() // 응답 처리 시작
                .bodyToMono(KeywordListDto.class) // 응답을 String으로 변환
                .map(res -> res.getItems())
                .block(); // 비동기 호출 완료를 기다림 (이 부분에서 응답 대기)\\

        for (KeywordItemDto item : items) {
            KeywordResponseDto keywordItem = KeywordResponseDto.builder()
                    .title(item.getTitle().replaceAll("<[^>]*>", ""))
                    .address(item.getRoadAddress())
                    .longitude(convertMapX(item.getMapx()))
                    .latitude(convertMapY(item.getMapy()))
                    .build();

            keywords.add(keywordItem);
        }

        return keywords;
    }

    public LocationListDto findLocations(FindLocationsRequestDto requestDto) {
        int total = (int) locationRepository.countByCondition(requestDto);

        // 예외 처리
        if (total == 0 || requestDto.getOffset() > total) {
            return new LocationListDto(requestDto.getOffset(), requestDto.getLimit(), total, new ArrayList<>());
        }

        // 1. 범주 내에 있는 장소를 모두 찾음
        List<Location> locationsInRange = locationRepository.findByRange(requestDto);

        // 2. review와 매핑하여 좋아요 수가 가장 높은 것을 기준으로 정렬 및 limit 걸기
        List<LocationDto> byLikeCountDesc = locationRepository.findByLikeCountDesc(requestDto, locationsInRange);

        return new LocationListDto(requestDto.getOffset(), requestDto.getLimit(), total, byLikeCountDesc);
    }

    public SingleLocationDto findLocation(Long locationId) {
        Location location = locationRepository.findByLocationId(locationId);

        if (location == null) {
            throw new NotFoundException("NOT_FOUND_LOCATION");
        }

        return SingleLocationDto.builder()
                .id(location.getId())
                .categoryId(location.getCategory().getId())
                .name(location.getName())
                .address(location.getAddress())
                .build();
    }

    public ReviewListDto findReviews(Long locationId, PageRequestDto pageDto) {
        int total = (int) reviewRepository.countReviews(locationId);

        if (total == 0 || pageDto.getOffset() > total) {
            return new ReviewListDto(pageDto.getOffset(), pageDto.getLimit(), total, new ArrayList<>());
        }

        List<Review> allByLocationId = reviewRepository.findAllByLocationId(locationId, pageDto);

        return new ReviewListDto(pageDto.getOffset(), pageDto.getLimit(), total, allByLocationId);
    }

    private static double convertMapX(String mapX) {
        double x = Double.parseDouble(mapX);
        return x / 10000000.0;
    }

    private static double convertMapY(String mapY) {
        double y = Double.parseDouble(mapY);
        return y / 10000000.0;
    }
}
