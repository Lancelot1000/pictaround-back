package project.pictaround.dto.request;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class PageRequestDto {
    private Integer limit;
    private Integer offset;

    public Integer getLimit() {
        return limit == null ? 20 : limit;
    }

    public Integer getOffset() {
        return offset == null ? 0 : offset;
    }
}
