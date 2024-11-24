package project.pictaround.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class LocationListDto {
    private int offset;
    private int limit;
    private int total;
    private List<LocationDto> items;
}
