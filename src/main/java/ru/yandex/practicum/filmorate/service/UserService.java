package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService implements UserServiceInterface {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User delete(Long id) {
        return userStorage.delete(id);
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long otherId) {
        log.info("Adding friend {} to user {} ", otherId, userId);
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(otherId);

        user.getFriends().add(otherId);
        friend.getFriends().add(userId);
    }

    public void deleteFriends(Long userId, Long friendId) {
        log.info("Adding friend {} to user {} ", friendId, userId);
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> allFriend(Long userId) {
        return findById(userId).getFriends().stream().map(userStorage::findById).toList();
    }

    public List<User> getMutualFriends(Long userId, Long friendId) {
        Set<Long> friendUser = findById(userId).getFriends();
        Set<Long> friend = findById(friendId).getFriends();

        return friend.stream().filter(friendUser::contains).map(userStorage::findById).toList();
    }

}
