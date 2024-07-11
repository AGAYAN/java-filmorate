package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    void addFriend(Long userId, Long otherId);

    void deleteFriends(Long userId, Long friendId);

    List<User> allFriend(Long userId);

    List<User> getMutualFriends(Long userId, Long friendId);
}
