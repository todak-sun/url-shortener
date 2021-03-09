package foo.study.url;

import foo.study.url.domain.UrlHashMapRepository;
import foo.study.url.domain.UrlRepository;
import foo.study.url.ifs.IdGenerator;
import foo.study.url.util.UUIDGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class UrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlApplication.class, args);
    }


    @Bean
    public UrlRepository urlRepository() {
        return new UrlHashMapRepository(idGenerator());
    }

    @Bean
    public IdGenerator idGenerator() {
        return new UUIDGenerator();
    }

}
