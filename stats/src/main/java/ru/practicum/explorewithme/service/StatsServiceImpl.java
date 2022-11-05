package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.repository.StatsRepository;
import ru.practicum.explorewithme.mapper.StatsMapper;
import ru.practicum.explorewithme.model.Hit;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(HitDto hitDto) {
        log.info("Save statistic for hit: {}", hitDto);
        Hit hit = StatsMapper.toHit(hitDto);
        statsRepository.save(hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uncodedUris, Boolean unique) {
        List<String> uris = new ArrayList<>();
        for (String u : uncodedUris) {
            uris.add(URLDecoder.decode(u, StandardCharsets.UTF_8));
        }
        log.info("Get statistic for uri list: {}", uris);
        List<Hit> hits = statsRepository.findDistinctHitsByUriInAndTimestampBetween(uris, start, end);
        List<ViewStats> viewStats = hits.stream()
                .map(h -> StatsMapper.toViewStats(h, hits.size()))
                .collect(Collectors.toList());
        if (viewStats.isEmpty()) {
            return List.of(new ViewStats("unavailable", "unavailable", 0));
        }
        return viewStats;
    }
}
