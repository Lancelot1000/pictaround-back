package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordResponseDto {
    private String title;
    private String address;
    private double latitude;
    private double longitude;

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
