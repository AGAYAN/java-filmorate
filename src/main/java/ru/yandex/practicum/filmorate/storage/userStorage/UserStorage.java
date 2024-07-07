package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    void add(User user) throws ValidationException;

    void update(User user);

    void delete(int id);

    User findById(Long id);

    Collection<User> findAll();

    long getNextId();

    void addFriend(Long aLong, Long id);

    void deleteFriends(Long userId, Long friendId);

    Set<Long> allFriend(Long userId);

    List<Long> getMutualFriends(Long userId, Long friendId);
}