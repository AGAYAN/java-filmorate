package ru.yandex.practicum.filmorate.storage.userStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public User add(User user) {

        user.setName(user.getLogin());
        user.setId(getNextId());
        userMap.put(user.getId(), user);

        log.info("Пользователь с именем: {} создан", user.getName());
        return user;
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

    public Collection<User> findAll() {
        log.info("Всего пользователей: {} ", userMap.values().size());
        return userMap.values();
    }

    @Override
    public void update(User user) {
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

    @Override
    public long getNextId() {
        long currentMaxId = userMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
