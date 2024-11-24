package project.pictaround.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Favorite;
import project.pictaround.domain.Member;
import project.pictaround.domain.Review;
import project.pictaround.dto.response.MyFavoriteDto;
import project.pictaround.exception.NotFoundException;
import project.pictaround.exception.UnauthorizedException;
import project.pictaround.repository.FavoriteRepository;
import project.pictaround.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SessionService sessionService;
    private final ReviewRepository reviewRepository;


    public MyFavoriteDto findFavorite(HttpServletRequest request, HttpServletResponse response) {
        Member member = sessionService.findMember(request, response, false);

        if (member == null) {
            return MyFavoriteDto.of(new ArrayList<>());
        }

        List<Favorite> byMemberId = favoriteRepository.findByMemberId(member);

        return MyFavoriteDto.of(byMemberId);
    }

    @Transactional
    public void setFavorite(Long reviewId, HttpServletRequest request, HttpServletResponse response) {
        Member member = sessionService.findMember(request, response, false);
        Review review = reviewRepository.findById(reviewId);

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        if (review == null) {
            throw new NotFoundException("NOT_FOUND_REVIEW");
        }

        String queryType = favoriteRepository.setFavorite(review, member);
        updateLikeCount(review, queryType);
    }

    public void updateLikeCount(Review review, String type) {
        int calculated = type.equals("DELETE") ? -1 : 1;

        reviewRepository.updateLikeCount(review, review.getLikeCount() + calculated);
    }

    public Integer getFavoriteCount(Long reviewId) {
        Review review = reviewRepository.findById(reviewId);

        if (review == null) {
            throw new NotFoundException("NOT_FOUND_REVIEW");
        }

        return review.getLikeCount();
    }
}
