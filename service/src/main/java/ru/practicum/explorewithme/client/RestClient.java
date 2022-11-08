package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.event.model.Hit;
import ru.practicum.explorewithme.event.model.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.formatter;

@Slf4j
@RestController
public class RestClient {

    @Value("${stats-post.path}")
    private String hitPostPath;

    @Value("${stats-get.path}")
    private String hitGetPath;

    private RestTemplate rest;

    public RestClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void postHit(Hit hit) {
        log.info("Package sent to statistic service:{}", hit);
        rest.postForEntity(hitPostPath, hit, Hit.class);
    }

    public ViewStats getStats(int eventId, LocalDateTime start, LocalDateTime end) {
        log.info("Appeal to statistic service");
        ViewStats[] stats = rest.getForObject(
                String.format(
                        hitGetPath,
                        start.format(formatter),
                        end.format(formatter),
                        List.of(URLEncoder.encode("/events/" + eventId, StandardCharsets.UTF_8)),
                        "false"),
                ViewStats[].class
        );
        return stats != null ? stats[0] : null;
    }
}
