package project.pictaround.dto.request;

import lombok.Getter;
import project.pictaround.domain.Category;

@Getter
public class CategoryRequestDto {
    private String value;
    private String label;

    public static Category toEntity(CategoryRequestDto dto) {
        return Category.builder()
                .label(dto.getLabel())
                .name(dto.getValue())
                .build();
    }
}
