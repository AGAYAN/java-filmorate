package ru.yandex.practicum.filmorate.service.DbService;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        validation(user);
        return userStorage.add(user);
    }

    public void addNewFriend(Long userId, Long friendId) {
        userStorage.addNewFriend(userId, friendId);
    }

    public User getUserById(Long id) {
        return userStorage.findById(id);
    }

    public List<User> getAllUser() {
        return (List<User>) userStorage.findAll();
    }

    public Set<User> allFriend(Long userId) {
        return userStorage.allFriend(userId);
    }

    public List<User> getMutualFriend(Long userId, Long friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }

    public User updateUser(User user) {
        validation(user);
        return userStorage.update(user);
    }

    public void deleteUser(Long id) {
        userStorage.delete(id);
    }

    public void deleteFriendFromUser(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    private void validation(User user) throws ValidationException {
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
}
