package ru.yandex.practicum.filmorate.storage.userStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();
    private long nextId = 0;

    @Override
    public void add(User user) throws ValidationException {
        validate(user);

        user.setName(user.getLogin());
        user.setId(getNextId());
        userMap.put(user.getId(), user);

        log.info("Пользователь с именем: {} создан", user.getName());
    }

    @Override
    public void delete(int id) {
        User userToRemove = userMap.remove(id);
        if (userToRemove == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public User findById(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public long getNextId() {
        return ++nextId;
    }

    @Override
    public void update(User user) throws ValidationException {
        validate(user);
        userMap.put(user.getId(), user);
    }

    @Override
    public void addFriend(Long userId, Long friendUserId) {
        findById(userId).getFriends().add(friendUserId);
        findById(friendUserId).getFriends().add(userId);
    }

    @Override
    public void deleteFriends(Long userId, Long friendId) {
        findById(userId).getFriends().remove(friendId);
        findById(friendId).getFriends().remove(userId);
    }

    @Override
    public Set<Long> allFriend(Long userId) {
        return findById(userId).getFriends();
    }

    @Override
    public List<Long> getMutualFriends(Long userId, Long friendId) {
        Set<Long> friendUser = findById(userId).getFriends();
        Set<Long> friend = findById(friendId).getFriends();

        return friend.stream().filter(friendUser::contains).toList();
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
}
