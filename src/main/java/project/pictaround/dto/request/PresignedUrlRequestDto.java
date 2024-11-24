package project.pictaround.dto.request;

import lombok.Getter;

@Getter
public class PresignedUrlRequestDto {
    private String route;
    private String filename;
    private String extension;
}
