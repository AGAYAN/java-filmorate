package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController controller;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    @Test
    public void addUser() throws ValidationException {
        Film film = Film.builder()
                .description("description")
                .name("1+1")
                .releaseDate(LocalDate.parse("23.05.2016", formatter))
                .duration(Duration.ofMinutes(90))
                .build();


        controller.addFilm(film);
        assertEquals(1, film.getId());
    }

    @Test
    public void getFilmId() {
        assertNotNull(controller.getFilms());
    }
}
