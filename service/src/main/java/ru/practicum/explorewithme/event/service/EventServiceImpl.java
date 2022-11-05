package ru.practicum.explorewithme.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.client.RestClient;
import ru.practicum.explorewithme.utility.QPredicates;
import ru.practicum.explorewithme.utility.ValidationService;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.model.Status;
import ru.practicum.explorewithme.request.service.RequestService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.event.model.QEvent.event;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final RequestService requestService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ValidationService validationService;
    private final RestClient restClient;

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getShortDtoById(Long id, HttpServletRequest request) {
        log.info("Getting event with id {}", id);
        restClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DATE_TIME_FORMATTER))
        );
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Event with id %s is not found", id)));
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        log.info("Getting event with id {}", id);
        return eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Event with id %s is not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAll(FilterParams params, String sort, int from, int size, HttpServletRequest request) {
        log.info("Getting all events with filters: {}", params);
        restClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DATE_TIME_FORMATTER))
        );

        Predicate predicate = QPredicates.builder()
                .add(params.getText(), txt -> event.annotation.containsIgnoreCase(txt)
                        .or(event.description.containsIgnoreCase(txt)))
                .add(params.getCategories(), event.category.id::in)
                .add(params.getRangeStart(), event.eventDate::after)
                .add(params.getRangeEnd(), event.eventDate::before)
                .add(params.getPaid(), event.paid::eq)
                .buildAnd();
        List<Event> events = eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();
        if (Boolean.TRUE.equals(params.getOnlyAvailable())) {
            events = events.stream()
                    .filter(e -> (e.getParticipantLimit() != 0L)
                            && (e.getParticipantLimit() >
                            requestService.getRequestsByStatus(e.getId(), Status.CONFIRMED).size()))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUser(Long id, int from, int size) {
        log.info("Getting all events by user with id {}", id);
        List<Event> eventList = eventRepository.findEventsByInitiatorId(id, PageRequest.of(from, size));
        return eventList.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, Long id) {
        log.info("Adding event: {}", newEventDto);
        validationService.validateDeadline(newEventDto.getEventDate(), 2);
        Category category = categoryService.getCategory(newEventDto.getCategory());
        User initiator = userService.getUserById(id);
        Event newEvent = EventMapper.toEvent(newEventDto, category, initiator);
        return toFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest) {
        log.info("Updating event: {}", updateEventRequest);
        Event event = getEventById(updateEventRequest.getEventId());
        validationService.validateEventForUpdate(userId, updateEventRequest, event);
        return updateEventFields(updateEventRequest, event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        log.info("Getting event with id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return toFullDto(event);
    }

    @Override
    public EventFullDto cancelEventByUser(Long userId, Long eventId) {
        log.info("Cancel event with id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validatePendingState(event);
        event.setState(State.CANCELED);
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Getting request for event with id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.getAllRequestsForEvent(eventId);
    }

    @Override
    public RequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        log.info("Confirm request with id: {} for event with id: {}", requestId, eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.confirmRequest(event, userId, requestId);
    }

    @Override
    public RequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        log.info("Reject request with id: {} for event with id: {}", requestId, eventId);
        Event event = getEventById(eventId);
        validationService.validateAccessToEvent(userId, eventId, event);
        return requestService.rejectRequest(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllByFilter(List<Long> listUserId, List<State> states, List<Long> listCategoryId,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("Getting all events with filters: users {}, states {}, categories {}, start {}, end {},",
                listUserId, states, listCategoryId, rangeStart, rangeEnd);
        Predicate predicate = QPredicates.builder()
                .add(listUserId, event.initiator.id::in)
                .add(states, event.state::in)
                .add(listCategoryId, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .buildAnd();
        List<Event> events = eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();
        return events.stream()
                .map(this::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventRequest eventDto) {
        log.info("Uodating event: {}", eventDto);
        Event event = getEventById(eventId);
        return updateEventFields(eventDto, event);
    }

    @Override
    public EventFullDto publishByAdmin(Long eventId) {
        log.info("Publishing event with id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validateDeadline(event.getEventDate(), 1);
        validationService.validatePendingState(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return toFullDto(event);
    }

    @Override
    public EventFullDto reject(Long eventId) {
        log.info("Cancel event with id: {}", eventId);
        Event event = getEventById(eventId);
        validationService.validatePublishedState(event);
        event.setState(State.CANCELED);
        return toFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsCompilation(NewCompilationDto newCompilationDto) {
        log.info("Getting compilation with id: {}", newCompilationDto.getEvents());
        return new ArrayList<>(eventRepository.findEventsByIdIn(newCompilationDto.getEvents()));
    }

    private EventFullDto updateEventFields(UpdateEventRequest updateEventRequest, Event event) {
        log.info("Updating fields in evet with id: {}", event.getId());
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryService.getCategory(updateEventRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit().intValue());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        return toFullDto(eventRepository.save(event));
    }

    private EventShortDto toShortDto(Event event) {
        log.info("Convertation event: {} to EventShortDto", event);
        int confirmedRequestsCount = requestService.getRequestsByStatus(event.getId(), Status.CONFIRMED).size();
        ViewStats viewStats = restClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        return EventMapper.toEventShortDto(event, confirmedRequestsCount, viewStats.getHits().intValue());
    }

    private EventFullDto toFullDto(Event event) {
        log.info("Convertation event: {} to EventFullDto", event);
        int confirmedRequestsCount = requestService.getRequestsByStatus(event.getId(), Status.CONFIRMED).size();
        ViewStats viewStats = restClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        return EventMapper.toEventFullDto(event, confirmedRequestsCount, viewStats.getHits().intValue());
    }
}
