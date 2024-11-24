package project.pictaround.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.pictaround.dto.request.PresignedUrlRequestDto;
import project.pictaround.dto.response.PresignedUrlResponseDto;
import project.pictaround.service.S3Uploader;

import java.util.Map;

@Controller
public class S3Controller {

    private S3Uploader s3Uploader;

    public S3Controller(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @PostMapping("/get-presigned-url")
    public ResponseEntity<PresignedUrlResponseDto> getPresignedUrl(@RequestBody PresignedUrlRequestDto requestDto) {
        Map<String, String> response = s3Uploader.createPresignedUrl(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(PresignedUrlResponseDto.of(response));
    }
}
