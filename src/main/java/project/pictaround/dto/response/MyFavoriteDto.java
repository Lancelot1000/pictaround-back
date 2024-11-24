package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;
import project.pictaround.domain.Favorite;

import java.util.List;

@Getter
@Builder
public class MyFavoriteDto {
    private List<Long> favoriteReviewIds;


    public static MyFavoriteDto of(List<Favorite> favorite) {
        return MyFavoriteDto.builder()
                .favoriteReviewIds(favorite.stream().map(e -> e.getReview().getId()).toList())
                .build();
    }
}
