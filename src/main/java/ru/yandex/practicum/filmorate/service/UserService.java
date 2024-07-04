package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void add(User user) throws ValidationException {
        userStorage.add(user);
    }

    public void update(User user) throws ValidationException {
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

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriends(userId, friendId);
    }

    public Set<Long> allFriend(Long userId) {
        return userStorage.allFriend(userId);
    }

    public List<Long> getMutualFriends(Long userId, Long friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }
}
