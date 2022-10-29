package ru.practicum.explore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.service.StatsService;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер статистики
 */
@RestController
public class StatsController {
    private StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /*
    Метод контроллера по сохранению статистики
    */
    @PostMapping("/hit")
    public EndpointHit save(@RequestBody EndpointHit endpointHit) {
        return statsService.save(endpointHit);
    }

    /*
    Метод контроллера по получению статистики
    */
    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
