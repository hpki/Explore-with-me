package ru.practicum.explorewithme.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.model.Status;

import java.util.List;

public interface RequestService {

    // Получение запросов пользователем
    List<RequestDto> getAll(Long userId);

    // Добавление запроса пользователя на событие
    RequestDto addRequest(Long userId, Event event);

    // Обновление запроса
    RequestDto updateRequest(Long userId, Long requestId);

    // Список всех запросов по статусу
    List<Request> getRequestsByStatus(Long id, Status status);

    // Список всех запросов на событие
    List<RequestDto> getAllRequestsForEvent(Long eventId);

    // Отмена запроса
    RequestDto rejectRequest(Long requestId);

    // Подтверждение запроса
    RequestDto confirmRequest(Event event, Long userId, Long requestId);

    List<Event> getAllUserEventsWithConfirmedParticipation(Long id, PageRequest pageRequest);
}
