package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void add(User user) throws ValidationException {
        validate(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        userStorage.add(user);
    }

    public void update(User user) {
        validate(user);
        userStorage.update(user);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public User findById(long id) {
        return userStorage.findById(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriends(Long id, Long otherId) {
        containsUser(id);
        containsUser(otherId);
        userStorage.addFriend(id, otherId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        containsUser(userId);
        containsUser(friendId);
        userStorage.deleteFriends(userId, friendId);
    }

    public Set<Long> allFriend(Long userId) {
        containsUser(userId);
        return userStorage.allFriend(userId);
    }

    public List<Long> getMutualFriends(Long userId, Long friendId) {
        containsUser(userId);
        containsUser(friendId);
        return userStorage.getMutualFriends(userId, friendId);
    }

    public void validate(User user) throws ValidationException {
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

    }

    private void containsUser(Long userId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("Пользователя с id =  " + userId
                    + " нет в системе");
        }
    }
}
