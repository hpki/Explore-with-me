package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.List;

public interface UserService {

    // Получение списка всех пользователей
    List<UserDto> getAllUsers(List<Long> listId, int from, int size);

    // Получение пользователя по id
    User getUserById(Long id);

    // Добавление пользователя
    UserDto addUser(NewUserDto newUserDto);

    // Удаление пользователя
    void deleteUser(Long id);
}
