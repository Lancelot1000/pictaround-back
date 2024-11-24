package project.pictaround;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import project.pictaround.repository.CategoryRepository;
import project.pictaround.repository.LocationRepository;
import project.pictaround.repository.MemberRepository;
import project.pictaround.repository.ReviewRepository;

@SpringBootApplication
public class PictaroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictaroundApplication.class, args);
    }

    @Bean
    @Profile("local_create")
    public TestDataInit testDataInit(CategoryRepository categoryRepository, MemberRepository memberRepository, ReviewRepository reviewRepository, LocationRepository locationRepository) {
        return new TestDataInit(categoryRepository, memberRepository, reviewRepository, locationRepository);
    }

}
