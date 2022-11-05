package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;

    // Метод контроллера по сохранению статистики
    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody HitDto dto) {
        log.info("Getting POST request for statistic save: {}", dto);
        statsService.saveHit(dto);
    }

    // Метод контроллера по получению статистики
    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Getting GET request for statistic");
        return statsService.getStats(start, end, uris, unique);
    }
}