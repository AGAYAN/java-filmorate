
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id) {
        return userService.delete(id);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.findAll();
    }

    @PutMapping("/{userId}/friends/{friendsId}")
    public void addNewFriend(@PathVariable Long userId, @PathVariable Long friendsId) {
        userService.addFriend(userId, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        userService.deleteFriends(id, friendsId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriend(@PathVariable Long id) {
        return userService.allFriend(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long friendId) {
        return userService.getMutualFriends(userId, friendId);
    }
}
