package project.pictaround.dto.response;

import lombok.*;
import project.pictaround.domain.Category;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String label;
    private String value;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.value = category.getName();
        this.label = category.getLabel();
    }

    public static CategoryResponseDto of(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .label(category.getLabel())
                .value(category.getName())
                .build();
    }

}
