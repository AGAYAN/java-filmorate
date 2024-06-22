package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private UserController controller;

    @Test
    public void addNewUser() throws ValidationException {
        User user = User.builder()
                .login("dsadsa")
                .email("dasdas@mail.ru")
                .birthday(LocalDate.parse("23.06.2000", formatter))
                .build();

        controller.addNewUser(user);

        assertEquals(1, user.getId());
    }

    @Test
    public void updateUser() throws ValidationException {
        long id = 1;
        User user1;

        User user = User.builder()
                .id(id)
                .name("AGAYAN")
                .email("AGAIIIA@yandex.ru")
                .login("Test")
                .birthday(LocalDate.parse("15.04.2004", formatter))
                .build();

        controller.update(user);

        user1 = controller.getUser(id);

        assertEquals("AGAYAN", user1.getName());
        assertEquals("AGAIIIA@yandex.ru", user1.getEmail());
        assertEquals("Test", user1.getLogin());
        assertEquals(LocalDate.parse("15.04.2004", formatter), user1.getBirthday());
    }
}