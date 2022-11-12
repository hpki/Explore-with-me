package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    // Получение dto события по id
    EventFullDto getShortDtoById(Long id, HttpServletRequest request);

    // Получение события по id
    Event getEventById(Long id);

    // Получение всех short событий  по фильтрам
    List<EventShortDto> getAll(FilterParams params, String sort, int from, int size, HttpServletRequest request);

    // Получение списка событий пользователя
    List<EventShortDto> getEventsByUser(Long id, int from, int size);

    // Добавление события
    EventFullDto addEvent(NewEventDto newEventDto, Long id);

    // Обновление события пользователя
    EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest);

    // Получение события
    EventFullDto getUserEvent(Long userId, Long eventId);

    // Отмена события
    EventFullDto cancelEventByUser(Long userId, Long eventId);

    // Получение списка заявок на событие
    List<RequestDto> getEventRequests(Long id, Long eventId);

    // Подтверждение заявки
    RequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    // Отклонение заявки
    RequestDto rejectRequest(Long userId, Long eventId, Long requestId);

    // Получение всех full событий  по фильтрам
    List<EventFullDto> getAllByFilter(List<Long> listUserId, List<State> states, List<Long> listCategoryId,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    // Обновление события админом
    EventFullDto updateByAdmin(Long eventId, UpdateEventRequest eventDto);

    // Публикация события админом
    EventFullDto publishByAdmin(Long eventId);

    // Отклонение события
    EventFullDto reject(Long eventId);

    // Получение списка событий
    List<Event> getEventsCompilation(NewCompilationDto newCompilationDto);

    List<EventShortDto> getEventsUserCreatedOrJoined(Long id, int from, int size);

    List<EventShortDto> getAllUserFriendsEvents(List<Long> friendIds, int from, int size);

}
