package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void add(User user) throws ValidationException {
        validate(user);
        userStorage.add(user);
    }

    public void update(User user) throws ValidationException {
        validate(user);
        userStorage.update(user);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public User findById(long id) {
        return userStorage.findById(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriends(Long id, Long otherId) throws ValidationException {
        userStorage.addFriend(id, otherId);
    }

    public void deleteFriend(Long userId, Long friendId) throws ValidationException {
        containsUser(userId);
        userStorage.deleteFriends(userId, friendId);
    }

    public Set<Long> allFriend(Long userId) throws ValidationException {
        containsUser(userId);
        return userStorage.allFriend(userId);
    }

    public List<Long> getMutualFriends(Long userId, Long friendId) throws ValidationException {
        containsUser(userId);
        return userStorage.getMutualFriends(userId, friendId);
    }

    public void validate(User user) throws ValidationException {
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
    }

    private void containsUser(Long userId) throws ValidationException {
        if (userStorage.findById(userId) == null) {
            throw new ValidationException("Пользователя с id =  " + userId
                    + " нет в системе");
        }
    }
}
