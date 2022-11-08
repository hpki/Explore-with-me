package ru.practicum.explorewithme.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.NewUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.ID_PATH_VARIABLE_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    // Получение списка всех пользователей
    @GetMapping
    public List<UserDto> getAllById(@RequestParam List<Long> ids,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info("Getting request GET for path /admin/users");
        return userService.getAllUsers(ids, from, size);
    }

    // Добавление пользователя
    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserDto newUserDto) {
        log.info("Getting request POST for path /admin/users for adding user: {}", newUserDto);
        return userService.addUser(newUserDto);
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request DELETE for path /admin/users for user with id {}", id);
        userService.deleteUser(id);
    }
}