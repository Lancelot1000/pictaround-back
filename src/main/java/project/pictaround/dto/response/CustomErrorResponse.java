package project.pictaround.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {
    private String message;
    private boolean success;

    public CustomErrorResponse(String message) {
        this.message = message;
        this.success = false;
    }
}
