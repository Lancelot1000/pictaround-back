package project.pictaround.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class FindLocationsRequestDto {
    @Getter
    private float minLng;
    @Getter
    private float maxLng;
    @Getter
    private float minLat;
    @Getter
    private float maxLat;

    private Integer offset;
    private Integer limit;
    private String category;

    public int getOffset() {
        return offset != null ? offset : 0; // 기본값 0
    }

    public int getLimit() {
        return limit != null ? limit : 20; // 기본값 10
    }

    public String getCategory() {
        return category != null ? category : "all"; // 기본값
    }

    @Override
    public String toString() {
        return "FindLocationsRequestDto{" +
                "minLng='" + minLng + '\'' +
                ", maxLng=" + maxLng +
                ", minLat=" + minLat +
                ", maxLat=" + maxLat +
                ", offset=" + offset +
                ", limit=" + limit +
                ", category='" + category + '\'' +
                '}';
    }
}
