package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.dto.RequestDto;

import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{id}/events/{eventId}/requests")
public class UserRequestController {

    public static final String REQ_ID_PATH_VARIABLE_KEY = "reqId";

    private final EventService eventService;

    @GetMapping
    public List<RequestDto> getEventRequests(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                             @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Getting request GET for path /users/{}/events/{}/requests", id, eventId);
        return eventService.getEventRequests(id, eventId);
    }

    @PatchMapping("/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                     @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                     @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Getting request PATCH for path /users/{}/events/{}/requests/{}/confirm", id, eventId, requestId);
        return eventService.confirmRequest(id, eventId, requestId);
    }

    @PatchMapping("/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable(USER_ID_PATH_VARIABLE_KEY) Long id,
                                    @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId,
                                    @PathVariable(REQ_ID_PATH_VARIABLE_KEY) Long requestId) {
        log.info("Getting request PATCH for path /users/{}/events/{}/requests/{}/reject", id, eventId, requestId);
        return eventService.rejectRequest(id, eventId, requestId);
    }
}
