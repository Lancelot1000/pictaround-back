package project.pictaround.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.pictaround.dto.request.PresignedUrlRequestDto;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class S3Uploader {

    @Value("${cloud.aws.s3.domain}")
    private String BUCKET_DOMAIN;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    S3Presigner s3Presigner;

    public S3Uploader(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }


    public Map<String, String> createPresignedUrl(PresignedUrlRequestDto requestDto) {
        String keyName = requestDto.getRoute() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS")) + "_" + requestDto.getFilename();
        keyName = keyName.replaceAll("-", "");
        keyName = keyName.substring(0, Math.min(200, keyName.length()));

        String contentType = "image/" + requestDto.getExtension();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(keyName)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String myURL = presignedRequest.url().toString();

        myURL = myURL.replace(BUCKET_DOMAIN, "");
        log.info("presigned url to upload a file to : {}",myURL);
        log.info("HTTP method: {}", presignedRequest.httpRequest().method());

        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("uploadUrl", myURL);
        map.put("location", keyName);

        return map;
    }
}
