package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.repository.UserRepository;
import ru.practicum.explorewithme.user.dto.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(List<Long> listId, int from, int size) {
        log.info("Getting all users list");
        Pageable pageable = PageRequest.of(from, size);
        List<User> userList = userRepository.findUsersByIdIn(listId, pageable);
        return userList.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        log.info("Getting user with id {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("user with id %s is not found", id)));
    }

    @Override
    public UserDto addUser(NewUserDto newUserDto) {
        log.info("Adding user: {}", newUserDto);
        User user = UserMapper.toUser(newUserDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id {}", id);
        User user = getUserById(id);
        userRepository.delete(user);
        log.info("User with id {} deleted", id);
    }
}
