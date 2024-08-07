package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User delete(Long id);

    User findById(Long id);

    Collection<User> findAll();

    long getNextId();

}