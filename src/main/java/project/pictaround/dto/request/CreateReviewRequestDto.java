package project.pictaround.dto.request;

import lombok.Getter;
import project.pictaround.domain.Category;
import project.pictaround.domain.Location;
import project.pictaround.domain.Member;
import project.pictaround.domain.Review;
import project.pictaround.dto.response.KeywordResponseDto;

import java.time.LocalDateTime;

@Getter
public class CreateReviewRequestDto {
    private Long categoryId;
    private String imageLocation;
    private String comment;
    private KeywordResponseDto location;

    public static Location toLocationEntity(CreateReviewRequestDto dto, Category category) {
        return Location.builder()
                .address(dto.getLocation().getAddress())
                .latitude(dto.getLocation().getLatitude())
                .longitude(dto.getLocation().getLongitude())
                .name(dto.getLocation().getTitle())
                .category(category)
                .build();
    }

    public static Review toEntity(CreateReviewRequestDto dto, Member member, Location location) {
        return Review.builder()
                .comment(dto.getComment())
                .imageLocation(dto.getImageLocation())
                .createdDateTime(LocalDateTime.now())
                .likeCount(0)
                .member(member)
                .location(location)
                .build();
    }


}
