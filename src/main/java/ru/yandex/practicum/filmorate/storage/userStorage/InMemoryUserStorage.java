package ru.yandex.practicum.filmorate.storage.userStorage;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public User add(User user) {
        validation(user);
        long id = getNextId();
        user.setId(id);
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setFriends(new HashSet<>());
        userMap.put(id, user);
        log.info("Пользователь с именем: {} создан", user.getName());
        return userMap.get(id);
    }

    @Override
    public User delete(Long id) {
        User userToRemove = userMap.remove(id);
        if (userToRemove == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден");
        }
        return userToRemove;
    }

    @Override
    public User findById(Long id) {

        if (!userMap.containsKey(id)) {
            throw new NotFoundException("Нет такого пользователя");
        }
        return userMap.get(id);
    }

    public Collection<User> findAll() {
        log.info("Всего пользователей: {} ", userMap.values().size());
        return userMap.values();
    }

    @Override
    public User update(User user) {
        validation(user);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Юзер с ID: {} успешно обновлен", user.getId());
            return userMap.get(user.getId());
        } else {
            log.error("Ошибка при обновлении юзера");
            throw new NotFoundException("Юзер с ID: " + user.getId() + " не найден");
        }
    }

    private void validation(User user) {
        if (StringUtils.isBlank(user.getEmail())) {
            throw new ValidationException("Электронная почта не может быть пустой");
        } else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", user.getEmail())) {
            throw new ValidationException("Неверный формат почты");
        } else if (StringUtils.isBlank(user.getLogin())) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    @Override
    public long getNextId() {
        long currentMaxId = userMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
