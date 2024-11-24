package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordResponseDto {
    private String title;
    private String address;
    private float latitude;
    private float longitude;

    @Override
    public String toString() {
        return "KeywordResponseDto{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
