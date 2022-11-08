package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    // сохранение статистики
    void saveHit(HitDto hitDto);

    // получение статистики
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
