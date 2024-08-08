package ru.yandex.practicum.filmorate.DbController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.DbService.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friendsId") Long friendsId) {
        userService.addNewFriend(userId, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friendsId") Long friendsId) {
        userService.deleteFriendFromUser(userId, friendsId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getAllFriends(@PathVariable("id") Long userId) {
        return userService.allFriend(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long friendId) {
        return userService.getMutualFriend(userId, friendId);
    }
}