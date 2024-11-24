package project.pictaround.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.pictaround.domain.Review;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ReviewListDto {
    private int offset;
    private int limit;
    private int total;
    private List<Review> items;
}
