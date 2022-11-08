package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.EVENT_ID_PATH_VARIABLE_KEY;
import static ru.practicum.explorewithme.EwmMainService.USER_ID_PATH_VARIABLE_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{id}/events")
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                               @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info("Getting request GET for path /users/{}/events", id);
        return eventService.getEventsByUser(id, from, size);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                        @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Getting request PATCH for path /users/{}/events", id);
        return eventService.updateUserEvent(id, eventDto);
    }

    @PostMapping
    public EventFullDto addEventByUser(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                       @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Getting request POST for path /users/{}/events", id);
        return eventService.addEvent(newEventDto, id);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                     @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Getting request GET for path /users/{}/events/{}", id, eventId);
        return eventService.getUserEvent(id, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                          @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Getting request PATCH for path /users/{}/events/{}", id, eventId);
        return eventService.cancelEventByUser(id, eventId);
    }
}
