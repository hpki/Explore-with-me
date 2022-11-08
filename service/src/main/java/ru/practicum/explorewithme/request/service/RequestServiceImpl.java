package ru.practicum.explorewithme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.utility.ValidationService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.request.mapper.RequestMapper;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.model.Status;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserService userService;
    private final RequestRepository requestRepository;
    private final ValidationService validationService;

    @Override
    public List<RequestDto> getAll(Long userId) {
        log.info("Getting request user with id {} for not own events", userId);
        List<Request> requests = requestRepository.getAllByRequesterId(userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto addRequest(Long userId, Event event) {
        User user = userService.getUserById(userId);
        log.info("Adding request user with id {} for event with id {}", userId, event);
        Request request = requestRepository.getByRequesterIdAndEventId(userId, event.getId());
        validationService.validateNewRequest(event, userId, request, getRequestsByStatus(event.getId(), Status.CONFIRMED));
        Request newRequest = new Request(null, user, event, LocalDateTime.now(), Status.PENDING);
        if (!event.isRequestModeration()) {
            newRequest.setStatus(Status.CONFIRMED);
        }
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    @Transactional
    public RequestDto updateRequest(Long userId, Long requestId) {
        log.info("User with id {} update request with id {}", userId, requestId);
        Request request = getById(requestId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<Request> getRequestsByStatus(Long id, Status status) {
        log.info("Getting request list in status {} for event with id {}", status, id);
        return requestRepository.findByEventIdAndStatus(id, status);
    }

    @Override
    public List<RequestDto> getAllRequestsForEvent(Long eventId) {
        log.info("Getting request list for event with id {}", eventId);
        List<Request> requestList = requestRepository.getAllByEventId(eventId);
        return requestList.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto rejectRequest(Long requestId) {
        log.info("Reject request with id {}", requestId);
        Request request = getById(requestId);
        request.setStatus(Status.REJECTED);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto confirmRequest(Event event, Long userId, Long requestId) {
        log.info("Confirming request with id {}", requestId);
        Request request = getById(requestId);
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            addRequest(userId, event);
            throw new BadRequestException("Only pending or canceled events can be changed");
        }
        if (event.getParticipantLimit() == getRequestsByStatus(event.getId(), Status.CONFIRMED).size()) {
            List<Request> pendingRequests = getRequestsByStatus(event.getId(), Status.PENDING);
            for (Request r : pendingRequests) {
                r.setStatus(Status.REJECTED);
                addRequest(userId, event);
            }
            throw new BadRequestException("Limit for requests on event is full");
        }
        request.setStatus(Status.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private Request getById(Long id) {
        log.info("Getting request with id {}", id);
        return requestRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("request with id %s is not found", id)));
    }
}
