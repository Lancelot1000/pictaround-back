package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PresignedUrlResponseDto {
    private String presignedUrl;
    private String location;
    private boolean success;

    public static PresignedUrlResponseDto of(Map<String, String> map) {
        return PresignedUrlResponseDto.builder()
                .presignedUrl(map.getOrDefault("uploadUrl", ""))
                .location(map.getOrDefault("location", ""))
                .success(true)
                .build();
    }

}
