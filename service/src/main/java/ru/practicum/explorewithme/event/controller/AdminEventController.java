package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    public static final String USER_ID_PATH_VARIABLE_KEY = "id";

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllByFilter(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.info("Getting request GET for path /admin/events");
        return eventService.getAllByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{id}")
    public EventFullDto updateEventRequestByAdmin(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                                  @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Getting request PUT for path /admin/events/{}", id);
        return eventService.updateByAdmin(id, eventDto);
    }

    @PatchMapping("/{id}/publish")
    public EventFullDto publishEventRequestByAdmin(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request PATCH for path /admin/events/{}/publish", id);
        return eventService.publishByAdmin(id);
    }

    @PatchMapping("/{id}/reject")
    public EventFullDto reject(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request PATCH for path /admin/events/{}/reject", id);
        return eventService.reject(id);
    }
}
