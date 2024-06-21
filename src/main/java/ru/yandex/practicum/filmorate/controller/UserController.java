package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> userMap = new HashMap<>();

    @PostMapping
    public void addNewUser(@RequestBody User user) {

        try {
            if (user.getLogin().isBlank()) {
                throw new ValidationException(log + "Логин не может быть пустым");
            } else if (user.getEmail().isBlank()) {
                throw new ValidationException(log + "Email не может быть пустым");
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {} ", e.getMessage());
            return;
        }

        user.setName(user.getLogin());
        user.setId(getPersonId());
        userMap.put(user.getId(), user);

        log.info("Пользователь с именем: {} ", user.getName() + " создан");
    }

    @PutMapping
    public void update(@RequestBody User user) throws ValidationException {

        if (!userMap.containsKey(user.getId())) {
            log.error("Пользователь с идентефикатором: {} ", user.getId() + " не найден");
            return;
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException(log + "Логин не может быть пустым");
        } else if (user.getEmail().isBlank()) {
            throw new ValidationException(log + "Email не может быть пустым");
        } else if (user.getName().isBlank()) {
            throw new ValidationException(log + "Имя не может быть пустым");
        }

        log.info("Данные пользователя с идентефикатором и именем {} {} ", user.getId(), user.getName() + " обновлены");
        userMap.put(user.getId(), user);

    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userMap.get(id);
    }

    @GetMapping
    public Map<Long, User> getUsers() {
        return userMap;
    }

    private Long getPersonId() {
        OptionalLong maxIdOpt = userMap.keySet().stream().mapToLong(Long::longValue).max();
        return maxIdOpt.orElseGet(() -> 1L);
    }
}
