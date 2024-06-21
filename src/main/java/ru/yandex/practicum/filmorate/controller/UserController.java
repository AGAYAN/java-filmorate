package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.regex.Pattern;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> userMap = new HashMap<>();

    @PostMapping
    public void addNewUser(@RequestBody User user) {

        try {
            if (user.getEmail().isBlank()) {
                log.error("Электронная почта не может быть пустой");
                throw new ValidationException("Электронная почта не может быть пустой");
            } else if (!Pattern.matches("^[a-zA-Z0-9]+@+[a-z]+.+[a-z]$", user.getEmail())) {
                log.error("Неверный формат почты");
                throw new ValidationException("Неверный формат почты");
            } else if (user.getLogin().isBlank()) {
                log.error("Логин не может быть пустым");
                throw new ValidationException("Логин не может быть пустым");
            } else if (user.getLogin().contains(" ")) {
                log.error("Логин должен быть без пробелов");
                throw new ValidationException("Логин должен быть без пробелов");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем.");
                throw new ValidationException("Дата рождения не может быть в будущем.");
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

        if (user.getEmail().isBlank()) {
            log.error("Электронная почта не может быть пустой");
            throw new ValidationException("Электронная почта не может быть пустой");
        } else if (!Pattern.matches("^[a-zA-Z0-9]+@+[a-z]+.+[a-z]$", user.getEmail())) {
            log.error("Неверный формат почты");
            throw new ValidationException("Неверный формат почты");
        } else if (user.getLogin().isBlank()) {
            log.error("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            log.error("Логин должен быть без пробелов");
            throw new ValidationException("Логин должен быть без пробелов");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
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
