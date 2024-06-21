package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
    public void addNewUser(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        } else if (!Pattern.matches("^[a-zA-Z0-9]+@+[a-z]+.+[a-z]$", user.getEmail())) {
            throw new ValidationException("Неверный формат почты");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин должен быть без пробелов");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        user.setName(user.getLogin());
        user.setId(getPersonId());
        userMap.put(user.getId(), user);

        log.info("Пользователь с именем: {} создан", user.getName());
    }

    @PutMapping
    public void update(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        } else if (!Pattern.matches("^[a-zA-Z0-9]+@+[a-z]+.+[a-z]$", user.getEmail())) {
            throw new ValidationException("Неверный формат почты");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин должен быть без пробелов");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        userMap.put(user.getId(), user);

        log.info("Данные пользователя с идентефикатором и именем {} обновлены", user.getId());
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
