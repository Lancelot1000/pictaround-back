package project.pictaround.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SearchProperties {
    @Value("${api.naver.search.host}")
    private String host;
    @Value("${api.naver.search.id}")
    private String id;
    @Value("${api.naver.search.secret}")
    private String secret;
}
