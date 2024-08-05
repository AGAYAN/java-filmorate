package ru.yandex.practicum.filmorate.storage.userStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        log.info("Добавление нового пользователя в базу данных");
        String insertQuery = "INSERT INTO users (login, name, email, birthday) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            log.error("Не удалось сгенерировать ключ для нового пользователя");
            throw new NotFoundException("КЛЮЧ НЕ БЫЛ СГЕНЕРИРОВАН");
        }
        user.setId(generatedId.longValue());
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Обновление пользователя с ID {}", user.getId());
        final String updateQuery = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";

        int rowsUpdated = jdbcTemplate.update(updateQuery, user.getName(), user.getEmail(), user.getLogin(), Date.valueOf(user.getBirthday()), user.getId());

        if (rowsUpdated == 0) {
            log.warn("Пользователь с ID {} не найден или данные не были обновлены", user.getId());
            throw new NotFoundException("Невозможно обновить пользователя с id =" + user.getId());
        }

        return findById(user.getId());
    }

    @Override
    public User delete(Long id) {
        findById(id);
        log.info("Удаление пользователя с ID = {}", id);
        String deleteQuery = "DELETE FROM users WHERE user_id = ?;";
        jdbcTemplate.update(deleteQuery, id);
        return null;
    }

    @Override
    public User findById(Long id) {
        String selectQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(selectQuery, new Object[]{id}, UserMapper::mapRow);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    @Override
    public List<User> findAll() {
        log.info("Получение всех пользователей");
        String selectAllQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(selectAllQuery, UserMapper::mapRow);
    }

    public long getNextId() {
        String maxIdQuery = "SELECT MAX(user_id) + 1 FROM users";
        Long nextId = jdbcTemplate.queryForObject(maxIdQuery, Long.class);
        return nextId != null ? nextId : 1;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        log.info("Удаление друга");
        findById(userId);
        findById(friendId);
        log.info("Пользователь с ID = {} удалил друга с ID = {}", userId, friendId);
        final String deleteFriendQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(deleteFriendQuery, userId, friendId);
    }

    @Override
    public void addNewFriend(Long userId, Long friendId) {
        log.info("Добавление друга");
        findById(userId);
        findById(friendId);
        log.info("Пользователь с ID = {} добавил друга с ID = {}", userId, friendId);
        final String addFriendQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(addFriendQuery, userId, friendId);
    }

    @Override
    public Set<User> allFriend(Long userId) {
        findById(userId);
        log.info("Получение всех друзей пользователя с ID = {}", userId);
        final String friendsQuery = "SELECT users.* FROM users " + "INNER JOIN friends ON users.user_id = friends.friend_id " + "WHERE friends.user_id = ?";
        return new HashSet<>(jdbcTemplate.query(friendsQuery, UserMapper::mapRow, userId));
    }

    public List<User> getMutualFriends(Long userId, Long friendId) {
        findById(userId);
        findById(friendId);
        log.info("Поиск общих друзей");
        final String mutualFriendsQuery = "SELECT DISTINCT u.* FROM users u " + "INNER JOIN friends f1 ON u.user_id = f1.friend_id " + "INNER JOIN friends f2 ON u.user_id = f2.friend_id " + "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(mutualFriendsQuery, UserMapper::mapRow, friendId, userId);
    }
}
