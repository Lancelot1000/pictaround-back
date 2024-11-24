package project.pictaround.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.pictaround.domain.Location;

@Getter
@Builder
@AllArgsConstructor
public class LocationDto {
    private Long id;
    private Long categoryId;
    private String name;
    private float latitude;
    private float longitude;
    private String imageLocation;

    @Override
    public String toString() {
        return "LocationDto{" +
                "id=" + id +
                ", categoryId = " + categoryId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imageLocation='" + imageLocation + '\'' +
                '}';
    }
}