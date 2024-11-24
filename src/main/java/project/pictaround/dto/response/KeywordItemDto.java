package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KeywordItemDto {
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;
}
