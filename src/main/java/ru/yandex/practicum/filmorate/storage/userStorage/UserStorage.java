package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User delete(Long id);

    User findById(Long id);

    Collection<User> findAll();

    long getNextId();

    void deleteFriend(Long userId, Long friendId);

    void addNewFriend(Long userId, Long friendId);

    Set<User> allFriend(Long userId);

    public List<User> getMutualFriends(Long userId, Long friendId);

}