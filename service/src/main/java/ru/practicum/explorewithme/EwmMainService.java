package ru.practicum.explorewithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class EwmMainService {

    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
