package ru.practicum.explorewithme.utility;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValidationService {

    public void validateNewRequest(Event event, Long userId, Request request, List<Request> confirmedRequests) {
        if (request != null) {
            throw new BadRequestException("adding a repeat request is not allowed");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("adding request for participation in own event is not allowed");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("participation in unpublished event is not allowed");
        }
        if (event.getParticipantLimit() == confirmedRequests.size()) {
            throw new BadRequestException("achieved limit for event request");

        }
    }

    public void validateDeadline(LocalDateTime date, int hours) {
        if (date.minusHours(hours).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(String.format("for event less then %s houers", hours));
        }
    }

    public void validateEventForUpdate(Long userId, UpdateEventRequest eventDto, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("user with id %s have not acсess to event with id %s",
                    userId, event.getId()));
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("making changes to a published event is not allowed");
        }
        if (event.getState().equals(State.CANCELED)) {
            event.setState(State.PENDING);
        }
        if (eventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(String.format("for event less then %s houers", 2));
        }
    }

    public void validateAccessToEvent(Long userId, Long eventId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("user with id %s have not acсess to event with id %s",
                    userId, eventId));
        }
    }

    public void validatePendingState(Event event) {
        if (!event.getState().equals(State.PENDING)) {
            throw new BadRequestException("event must be in pending state");
        }
    }

    public void validatePublishedState(Event event) {
        if (event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("canceling event in published state is not allowed");
        }
    }

}
