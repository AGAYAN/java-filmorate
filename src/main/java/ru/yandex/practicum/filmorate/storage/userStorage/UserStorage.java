package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    void add(User user) throws ValidationException;
    void update(User user) throws ValidationException;
    void delete(int id);
    User findById(Long id);
    List<User> findAll();
    void validate(User user) throws ValidationException;
    long getNextId();
    void addFriend(Long aLong, Long id) throws ValidationException;
    void deleteFriends(Long userId, Long friendId);
    Set<Long> allFriend(Long userId);
    List<Long> getMutualFriends (Long userId,Long friendId);
}