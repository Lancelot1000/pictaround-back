package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SingleLocationDto {
    private Long id;
    private Long categoryId;
    private String name;
    private String address;

}
