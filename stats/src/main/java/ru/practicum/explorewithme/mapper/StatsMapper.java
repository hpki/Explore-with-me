package ru.practicum.explorewithme.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.App;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {

    public static ViewStats toViewStats(Hit hit, int countHits) {
        return new ViewStats(
                hit.getApp().getAppName(),
                hit.getUri(),
                countHits
        );
    }

    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                null,
                new App(null, hitDto.getApp()),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }
}
