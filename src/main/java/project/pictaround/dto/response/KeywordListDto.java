package project.pictaround.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class KeywordListDto {
    private Integer total;
    private Integer start;
    private Integer display;
    private List<KeywordItemDto> items;
}
