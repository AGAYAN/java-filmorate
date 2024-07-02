package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addNewUser(@RequestBody User user) throws ValidationException {
        userService.add(user);
    }

    @PutMapping
    public void update(@RequestBody User user) throws ValidationException {
        userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public void addNewFriend(@PathVariable Long id, @PathVariable Long friendsId) throws ValidationException {
        userService.addFriends(id,friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public void deleteFriend(@PathVariable Long userId, Long friendId) {
        userService.deleteFriend(userId,friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Long> getAllFriend (@PathVariable Long userId) {
        return userService.allFriend(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Long> getMutualFriends (@PathVariable("id") Long userId, @PathVariable("otherId") Long friendId) {
        return userService.getMutualFriends(userId,friendId);
    }
}
