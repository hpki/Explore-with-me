package ru.practicum.explorewithme.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final String NOT_A_FRIEND_MESSAGE = "Пользователь %s не входит в число друзей пользователя %s.";
    private static final String ACCESS_DENIED_MESSAGE = "Пользователь %s закрыл доступ к событиям со своим участием.";

     final UserService userService;
     final EventService eventService;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(Long userId, Long friendId, int from, int size) {
        log.info("Получение пользователем с id {} событий с участием пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        if (!user.getFriends().contains(friend)) {
            throw new ForbiddenException(String.format(NOT_A_FRIEND_MESSAGE, friendId, userId));
        }
        if (!friend.getPrivateMode()) {
            throw new ForbiddenException(String.format(ACCESS_DENIED_MESSAGE, friendId));
        }
        return eventService.getEventsUserCreatedOrJoined(friendId, from, size);
    }

    @Override
    public List<EventShortDto> getAllUserFriendsAvailableEvents(Long userId, int from, int size) {
        log.info("Получение всех доступных событий из подписки пользователя с id {}", userId);
        User user = userService.getById(userId);
        Set<User> friends = user.getFriends();
        List<Long> friendIds = friends.stream()
                .filter(u -> !u.getPrivateMode())
                .map(User::getId)
                .collect(Collectors.toList());
        return eventService.getAllUserFriendsEvents(friendIds, from, size);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("Добавление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        if (friend.getPrivateMode()) {
            throw new ForbiddenException(String.format(ACCESS_DENIED_MESSAGE, friendId));
        }
        log.info("Пользователь с id {} подписался на события пользователя с id {}", userId, friendId);
        user.getFriends().add(friend);
        //log.info("Пользователь с id {} подписался на события пользователя с id {}", userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        log.info("Удаление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        user.getFriends().remove(friend);
        log.info("Пользователь с id {} удалил подписку на события пользователя с id {}", userId, friendId);
    }

    @Override
    public void setPrivacy(Long id, boolean isPrivate) {
        log.info("Присвоение пользователю id {} уровня приватности: {}", id, isPrivate);
        User user = userService.getById(id);
        user.setPrivateMode(isPrivate);
        //log.info("Пользователь с id {} присвоил уровень приватности: {}", id, isPrivate);
        log.info("Пользователь с id {} изменил приватность", id);
    }
}
