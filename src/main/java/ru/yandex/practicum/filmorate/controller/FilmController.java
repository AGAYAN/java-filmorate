package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> filmMap = new HashMap<>();
    private long nextId = 0;

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        film.setId(getNextId());
        filmMap.put(getNextId(), film);

        return film;
    }

    @GetMapping
    public Map<Long, Film> getFilms() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return filmMap;
    }

    private long getNextId() {
        return ++nextId;
    }

    public void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if ((LocalDate.parse("15.02.2016", DateTimeFormatter.ofPattern("dd.MM.yyyy")).isBefore(film.getReleaseDate()))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration().getSeconds() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}
